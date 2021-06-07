package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.ApiResponse
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.utils.AppExecutors
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class FakeShowRepository constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors) :
    FakeShowDataSource {


    override fun getMovies(page: Int): LiveData<Resource<PagedList<ShowEntity>>> {
        return object : NetworkBoundResource<PagedList<ShowEntity>, List<ShowResponse>>(appExecutors) {
                public override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                    val config = PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(20)
                        .setPageSize(20)
                        .build()
                    return LivePagedListBuilder(localDataSource.getMovies(), config).build()
                }

                override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean =
                    data == null || data.isEmpty()

                public override fun createCall(): LiveData<ApiResponse<List<ShowResponse>>> {
                    val results = MutableLiveData<ApiResponse<List<ShowResponse>>>()
                    remoteDataSource.getMovies(
                        page,
                        object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>> {
                            override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                                results.postValue(data)
                            }

                            override fun onError(t: Throwable) {
                                throw t
                            }
                        })
                    return results
                }

                public override fun saveCallResult(data: List<ShowResponse>) {
                    val showList = ArrayList<ShowEntity>()
                    for (response in data) {
                        val show = ShowEntity(
                            response, Constant.SHOW_MOVIE
                        )
                        showList.add(show)
                    }
                    localDataSource.insertShows(showList)
                }
            }.asLiveData()
    }


    override fun getTvShows(page: Int): LiveData<Resource<PagedList<ShowEntity>>> {
        return object : NetworkBoundResource<PagedList<ShowEntity>, List<ShowResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(20)
                    .setPageSize(20)
                    .build()
                return LivePagedListBuilder(localDataSource.getTvShows(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean =
                data == null || data.isEmpty()

            public override fun createCall(): LiveData<ApiResponse<List<ShowResponse>>> {
                val results = MutableLiveData<ApiResponse<List<ShowResponse>>>()
                remoteDataSource.getTvShows(
                    page,
                    object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>> {
                        override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                            results.postValue(data)
                        }

                        override fun onError(t: Throwable) {
                            throw t
                        }
                    })
                return results
            }

            public override fun saveCallResult(data: List<ShowResponse>) {
                val showList = ArrayList<ShowEntity>()
                for (response in data) {
                    val show = ShowEntity(response, Constant.SHOW_TV)
                    showList.add(show)
                }
                localDataSource.insertShows(showList)
            }
        }.asLiveData()
    }

    override fun getShowDetail(type: String, showId: Long): LiveData<Resource<ShowEntity>> {
        return object : NetworkBoundResource<ShowEntity, ShowResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<ShowEntity> =
                localDataSource.getShow(type, showId)

            override fun shouldFetch(data: ShowEntity?): Boolean =
                true

            override fun createCall(): LiveData<ApiResponse<ShowResponse>> {
                val showLiveData = MutableLiveData<ApiResponse<ShowResponse>>()
                remoteDataSource.getShowDetail(
                    type,
                    showId,
                    object : RemoteDataSource.CustomCallback<ApiResponse<ShowResponse>> {
                        override fun onResponse(data: ApiResponse<ShowResponse>) {
                            showLiveData.postValue(data)
                        }

                        override fun onError(t: Throwable) {
                            throw t
                        }

                    }
                )
                return showLiveData
            }

            override fun saveCallResult(data: ShowResponse) {
                val localShow = localDataSource.getShowEntityById(data.movieDbId)
                val showResponse = ShowEntity(data, type)
                if (localShow != null) {
                    localShow.updateDataFromEntity(showResponse)
                    localDataSource.updateShow(localShow)
                } else {
                    localDataSource.insertShow(showResponse)
                }
            }
        }.asLiveData()
    }

}