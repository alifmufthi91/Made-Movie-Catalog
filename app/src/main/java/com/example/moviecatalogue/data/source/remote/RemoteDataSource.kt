package com.example.moviecatalogue.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.MainApplication
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.response.GenreListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RemoteDataSource @Inject constructor(
    val apiService: ApiService
) {

//    @Inject
//    lateinit var apiService: ApiService

    companion object {

        @Volatile
        private var instance: RemoteDataSource? = null

        const val SHOW_MOVIE = "Movie"
        const val SHOW_TV = "Tv"

//        fun getInstance(): RemoteDataSource =
//            instance ?: synchronized(this) {
//                instance ?: RemoteDataSource()
//            }
    }

    fun getMovies(page: Int): LiveData<ApiResponse<List<ShowResponse>>> {
        EspressoIdlingResource.increment()
        val resultShows = MutableLiveData<ApiResponse<List<ShowResponse>>>()
        apiService.showList(
            SHOW_MOVIE.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                val result = response.body()
                if (result != null) {
                    resultShows.postValue(ApiResponse.success(result.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
        return resultShows
    }

    fun getTvShows(page: Int): LiveData<ApiResponse<List<ShowResponse>>> {
        val resultShows = MutableLiveData<ApiResponse<List<ShowResponse>>>()
        EspressoIdlingResource.increment()
        apiService.showList(
            SHOW_TV.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                val result = response.body()
                if (result != null) {
                    resultShows.postValue(ApiResponse.success(result.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
        return resultShows
    }

    fun getSearchedShowByQuery(
        category: String,
        page: Int,
        query: String
    ): MutableLiveData<List<ShowEntity>>{
        val resultShows = MutableLiveData<List<ShowEntity>>()
        EspressoIdlingResource.increment()
        apiService.search(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            query
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                val result = response.body()
                val arrList = ArrayList<ShowEntity>()
                if (result != null) {
                    result.list.forEach {
                        val showEntity = newShowEntity(it, category)
                        arrList.add(showEntity)
                    }
                    resultShows.postValue(arrList)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
        return resultShows
    }

    fun getSearchedShowByGenre(
        category: String,
        page: Int,
        genre: String
    ): MutableLiveData<List<ShowEntity>> {
        val resultShows = MutableLiveData<List<ShowEntity>>()
        EspressoIdlingResource.increment()
        apiService.showList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            genre
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                val result = response.body()
                val arrList = ArrayList<ShowEntity>()
                if (result != null) {
                    result.list.forEach {
                        val showEntity = newShowEntity(it, category)
                        arrList.add(showEntity)
                    }
                    resultShows.postValue(arrList)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
        return resultShows
    }

    fun getShowDetail(type: String, showId: Int): MutableLiveData<ApiResponse<ShowResponse>> {
        EspressoIdlingResource.increment()
        val resultShow = MutableLiveData<ApiResponse<ShowResponse>>()
        when (type) {
            MovieFragment.SHOW_MOVIE -> showId.let {
                apiService.movie(it, BuildConfig.API_KEY)
                    .enqueue(object : Callback<ShowResponse>{
                        override fun onResponse(
                            call: Call<ShowResponse>,
                            response: Response<ShowResponse>
                        ) {
                            val result = response.body()
                            if (result != null) {
                                resultShow.postValue(ApiResponse.success(result))
                            }
                            EspressoIdlingResource.decrement()
                        }

                        override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                            EspressoIdlingResource.decrement()
                        }
                    })
            }
            else -> showId.let {
                apiService.tv(it, BuildConfig.API_KEY)
                .enqueue(object : Callback<ShowResponse>{
                    override fun onResponse(
                        call: Call<ShowResponse>,
                        response: Response<ShowResponse>
                    ) {
                        val result = response.body()
                        if (result != null) {
                            resultShow.postValue(ApiResponse.success(result))
                        }
                        EspressoIdlingResource.decrement()
                    }

                    override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                        EspressoIdlingResource.decrement()
                    }
                })
            }
        }
        return resultShow
    }

    fun getGenres(category: String): MutableLiveData<List<GenreEntity>>{
        EspressoIdlingResource.increment()
        val resultGenres = MutableLiveData<List<GenreEntity>>()
        apiService.getGenreList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY
        ).enqueue(object : Callback<GenreListResponse>{
            override fun onResponse(call: Call<GenreListResponse>, response: Response<GenreListResponse>) {
                val results = response.body()
                if (results != null) {
                    val genreList = ArrayList<GenreEntity>()
                    results.list.forEach {
                        val genre = GenreEntity(it.id, it.name, category)
                        genreList.add(genre)
                    }
                    resultGenres.postValue(genreList)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<GenreListResponse>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
        return resultGenres
    }

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