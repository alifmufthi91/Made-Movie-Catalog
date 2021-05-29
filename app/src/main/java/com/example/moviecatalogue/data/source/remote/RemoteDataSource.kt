package com.example.moviecatalogue.data.source.remote

import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.GenreList
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.model.ShowList
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.response.GenreListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.utils.EspressoIdlingResource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
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

    fun getMovies(page: Int, callback: CustomCallback<ArrayList<Show>>) {
        EspressoIdlingResource.increment()
        apiService.showList(
            SHOW_MOVIE.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(call: Call<ShowListResponse>, response: Response<ShowListResponse>) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getTvShows(page: Int, callback: CustomCallback<ArrayList<Show>>) {
        EspressoIdlingResource.increment()
        apiService.showList(
            SHOW_TV.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        ).enqueue(object : Callback<ShowListResponse>{
            override fun onResponse(call: Call<ShowListResponse>, response: Response<ShowListResponse>) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getSearchedShowByQuery(
        category: String,
        page: Int,
        query: String,
        callback: CustomCallback<ArrayList<Show>>
    ) {
        EspressoIdlingResource.increment()
        apiService.search(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            query
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(call: Call<ShowListResponse>, response: Response<ShowListResponse>) {
                response.body()?.let {
                    callback.onResponse(it.list)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getSearchedShowByGenre(
        category: String,
        page: Int,
        genre: String,
        callback: CustomCallback<ArrayList<Show>>
    ) {
        EspressoIdlingResource.increment()
        apiService.showList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            genre
        ).enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                response.body()?.let {
                    callback.onResponse(it.list)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getShowDetail(type: String, showId: Int, callback: CustomCallback<Show>) {
        EspressoIdlingResource.increment()
        val call = when (type) {
            MovieFragment.SHOW_MOVIE -> showId.let { apiService.movie(it, BuildConfig.API_KEY) }
            else -> showId.let { apiService.tv(it, BuildConfig.API_KEY) }
        }
        call.enqueue(object : Callback<Show> {
            override fun onResponse(call: Call<Show>, response: Response<Show>) {
                response.body()?.let {
                    callback.onResponse(it)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<Show>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getGenres(category: String, callback: CustomCallback<ArrayList<Genre>>) {
        EspressoIdlingResource.increment()
        apiService.getGenreList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY
        ).enqueue(object : Callback<GenreList> {
            override fun onResponse(call: Call<GenreList>, response: Response<GenreList>) {
                response.body()?.let{
                    callback.onResponse(it.list)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<GenreList>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    interface CustomCallback<T> {
        fun onResponse(data: T)
        fun onError(t: Throwable)
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