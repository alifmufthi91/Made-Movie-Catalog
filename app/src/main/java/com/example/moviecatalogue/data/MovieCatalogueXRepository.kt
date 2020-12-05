package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.ApiResponse
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.utils.AppExecutors
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
        const val SHOW_MOVIE = "Movie"
        const val SHOW_TV = "Tv"

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

                public override fun createCall(): LiveData<ApiResponse<List<ShowResponse>>> =
                    remoteDataSource.getMovies(page)

                public override fun saveCallResult(showResponses: List<ShowResponse>) {
                    val showList = ArrayList<ShowEntity>()
                    for (response in showResponses) {
                        val show = newShowEntity(
                            response, SHOW_MOVIE
                                .toLowerCase(Locale.getDefault())
                        )
                        showList.add(show)
                    }
                    localDataSource.insertShows(showList)
                }
            }.asLiveData()
        moviesLiveData = moviesData
    }

    //
    override fun loadMoreMovies(page: Int) {
        remoteDataSource.getMovies(page)
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

                public override fun createCall(): LiveData<ApiResponse<List<ShowResponse>>> =
                    remoteDataSource.getTvShows(page)

                public override fun saveCallResult(showResponses: List<ShowResponse>) {
                    val showList = ArrayList<ShowEntity>()
                    for (response in showResponses) {
                        val show = newShowEntity(response, SHOW_TV.toLowerCase(Locale.getDefault()))
                        showList.add(show)
                    }
                    localDataSource.insertShows(showList)
                }
            }.asLiveData()
        tvShowsLiveData = tvShowsData
    }

    //
    override fun loadMoreTvShows(page: Int) {
        remoteDataSource.getTvShows(page)
    }

    override fun setSearchedShowsByQuery(
        category: String,
        page: Int,
        query: String
    ) {

        searchedShowsLiveData = remoteDataSource.getSearchedShowByQuery(category, page, query)
    }

    override fun setSearchedShowsByGenre(
        category: String,
        page: Int,
        genre: String
    ) {
        searchedShowsLiveData = remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre
        )
    }


    //
    override fun loadMoreSearchedShowsByQuery(category: String, page: Int, query: String) {
        remoteDataSource.getSearchedShowByQuery(
            category,
            page,
            query
        )
    }

    //
    override fun loadMoreSearchedShowsByGenre(category: String, page: Int, genre: String) {
        remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre
        )
    }

    override fun setShowFavorited(show: ShowEntity, state: Boolean) =
        appExecutors.diskIO.execute { localDataSource.setFavouriteShow(show, state) }

    override fun getMovies(): LiveData<Resource<PagedList<ShowEntity>>> = moviesLiveData

    override fun getTvShows(): LiveData<Resource<PagedList<ShowEntity>>> = tvShowsLiveData

    override fun getShowDetail(type: String, showId: Int): LiveData<Resource<ShowEntity>> {
        return object : NetworkBoundResource<ShowEntity, ShowResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<ShowEntity> =
                localDataSource.getShow(type, showId)

            override fun shouldFetch(show: ShowEntity?): Boolean =
                true

            override fun createCall(): LiveData<ApiResponse<ShowResponse>> =
                remoteDataSource.getShowDetail(
                    type,
                    showId
                )

            override fun saveCallResult(showResponse: ShowResponse) {
                val show = newShowEntity(showResponse, type)
                localDataSource.insertShow(show)
            }
        }.asLiveData()
    }

    override fun getSearchedShows(): LiveData<List<ShowEntity>> = searchedShowsLiveData

    override fun setGenres(category: String) {
        genresLiveData = remoteDataSource.getGenres(category)
    }

    override fun getGenres(): LiveData<List<GenreEntity>> =
        genresLiveData

    fun newShowEntity(showResponse: ShowResponse, type: String): ShowEntity {
        var genres = ""
        showResponse.genreList?.forEachIndexed { index, genreResponse ->
            genres += if (index != 0) {
                ", " + genreResponse.name
            } else {
                genreResponse.name
            }
        }
        return ShowEntity(
            showResponse.movieDbId,
            showResponse.name,
            showResponse.vote_average,
            showResponse.aired_date,
            showResponse.imgPath,
            showResponse.overview,
            showResponse.popularity,
            showResponse.voter,
            genres,
            type
        )
    }
}