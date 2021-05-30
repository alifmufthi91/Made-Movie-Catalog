package com.example.moviecatalogue.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.ApiResponse
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.data.source.remote.response.GenreResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.utils.AppExecutors
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import com.example.moviecatalogue.vo.Resource
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MovieCatalogueXRepository @Inject constructor(
    val remoteDataSource: RemoteDataSource, val localDataSource: LocalDataSource,
    val appExecutors: AppExecutors
) :
    MovieCatalogueXDataSource {

    private lateinit var moviesLiveData: MutableLiveData<Resource<PagedList<ShowEntity>>>
    private lateinit var tvShowsLiveData: MutableLiveData<Resource<PagedList<ShowEntity>>>
    private lateinit var searchedShowsLiveData: MutableLiveData<List<ShowEntity>>
    private lateinit var genresLiveData: MutableLiveData<List<GenreEntity>>

    companion object {

        @Volatile
        private var instance: MovieCatalogueXRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): MovieCatalogueXRepository =
            instance ?: synchronized(this) {
                instance ?: MovieCatalogueXRepository(remoteData, localData, appExecutors)
            }
    }

    override fun setMovies(page: Int) {
        val moviesData =
            object : NetworkBoundResource<PagedList<ShowEntity>, List<ShowResponse>>(appExecutors) {
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
                    remoteDataSource.getMovies(page, object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>> {
                        override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                            results.postValue(data)
                        }

                        override fun onError(t: Throwable) {
                        }
                    })
                    return results
                }

                public override fun saveCallResult(data: List<ShowResponse>) {
                    val showList = ArrayList<ShowEntity>()
                    for (response in data) {
                        val show = ShowEntity(
                            response, SHOW_MOVIE
                        )
                        showList.add(show)
                    }
                    localDataSource.insertShows(showList)
                }
            }.asLiveData()
        moviesLiveData = moviesData
    }

    //
    override fun loadMoreMovies(page: Int): LiveData<ApiResponse<List<ShowResponse>>> {
        val results = MutableLiveData<ApiResponse<List<ShowResponse>>>()
        remoteDataSource.getMovies(page, object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>> {
            override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                results.postValue(data)
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return results
    }

    override fun setTvShows(page: Int) {
        val tvShowsData =
            object : NetworkBoundResource<PagedList<ShowEntity>, List<ShowResponse>>(appExecutors) {
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
                            }
                        })
                    return results
                }
                public override fun saveCallResult(data: List<ShowResponse>) {
                    val showList = ArrayList<ShowEntity>()
                    for (response in data) {
                        val show = ShowEntity(response, SHOW_TV)
                        showList.add(show)
                    }
                    localDataSource.insertShows(showList)
                }
            }.asLiveData()
        tvShowsLiveData = tvShowsData
    }

    //
    override fun loadMoreTvShows(page: Int) : LiveData<ApiResponse<List<ShowResponse>>>{
        val results = MutableLiveData<ApiResponse<List<ShowResponse>>>()
        remoteDataSource.getTvShows(page, object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>> {
            override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                results.postValue(data)
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        return results
    }

    override fun setSearchedShowsByQuery(
        category: String,
        page: Int,
        query: String
    ) {
        val resultLiveData = MutableLiveData<List<ShowEntity>>()
        remoteDataSource.getSearchedShowByQuery(category, page, query, object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>>{
            override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                val showList = ArrayList<ShowEntity>()
                for (response in data.body) {
                    val show = ShowEntity(response, category)
                    showList.add(show)
                }
                resultLiveData.postValue(showList)
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        searchedShowsLiveData = resultLiveData
    }

    override fun setSearchedShowsByGenre(
        category: String,
        page: Int,
        genre: String
    ) {
        val resultLiveData = MutableLiveData<List<ShowEntity>>()
        remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre,
            object : RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>>{
                override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                    val showList = ArrayList<ShowEntity>()
                    for (response in data.body) {
                        val show = ShowEntity(response, category)
                        showList.add(show)
                    }
                    resultLiveData.postValue(showList)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        searchedShowsLiveData = resultLiveData
    }


    //
    override fun loadMoreSearchedShowsByQuery(category: String, page: Int, query: String): LiveData<ApiResponse<List<ShowResponse>>> {
        val results = MutableLiveData<ApiResponse<List<ShowResponse>>>()
        remoteDataSource.getSearchedShowByQuery(
            category,
            page,
            query,
            object: RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>>{
                override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                    results.postValue(data)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
        return results
    }

    //
    override fun loadMoreSearchedShowsByGenre(category: String, page: Int, genre: String): LiveData<ApiResponse<List<ShowResponse>>> {
        val results = MutableLiveData<ApiResponse<List<ShowResponse>>>()
        remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre,
            object: RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>>{
                override fun onResponse(data: ApiResponse<List<ShowResponse>>) {
                    results.postValue(data)
                }

                override fun onError(t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
        return results
    }

    override fun updateShow(show: ShowEntity) =
        appExecutors.diskIO.execute { localDataSource.updateShow(show) }

    override fun getMovies(): LiveData<Resource<PagedList<ShowEntity>>> = moviesLiveData

    override fun getTvShows(): LiveData<Resource<PagedList<ShowEntity>>> = tvShowsLiveData

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
                            TODO("Not yet implemented")
                        }

                    }
                )
                return showLiveData
            }

            override fun saveCallResult(data: ShowResponse) {
                val localShow = localDataSource.getShowEntityById(data.movieDbId)
                val showResponse = ShowEntity(data, type)
                if(localShow != null){
                    Log.d("localshow:",localShow.toString())
                    localShow.updateDataFromEntity(showResponse)
                    localDataSource.updateShow(localShow)
                }else{
                    Log.d("resshow:",showResponse.toString())
                    localDataSource.insertShow(showResponse)
                }
            }
        }.asLiveData()
    }

    override fun getSearchedShows(): LiveData<List<ShowEntity>> = searchedShowsLiveData

    override fun setGenres(category: String) {
        val resultLiveData = MutableLiveData<List<GenreEntity>>()
        val arrGenres = ArrayList<GenreEntity>()
        remoteDataSource.getGenres(category, object: RemoteDataSource.CustomCallback<ApiResponse<List<GenreResponse>>>{
            override fun onResponse(data: ApiResponse<List<GenreResponse>>) {
                data.body.forEach {
                    arrGenres.add(GenreEntity(it.id, it.name, category))
                }
                resultLiveData.postValue(arrGenres)
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        genresLiveData = resultLiveData
    }

    override fun getGenres(): LiveData<List<GenreEntity>> =
        genresLiveData

    override fun getFavoriteMovies(): LiveData<PagedList<ShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(100)
            .build()
        return LivePagedListBuilder(localDataSource.getFavouriteShowsByType(SHOW_MOVIE
        ), config
        ).build()
    }

    override fun getFavoriteTvShows(): LiveData<PagedList<ShowEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(100)
            .build()
        return LivePagedListBuilder(localDataSource.getFavouriteShowsByType(SHOW_TV
        ), config
        ).build()
    }
}