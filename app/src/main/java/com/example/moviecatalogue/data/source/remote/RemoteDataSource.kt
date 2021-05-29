package com.example.moviecatalogue.data.source.remote

import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.GenreList
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.model.ShowList
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.utils.EspressoIdlingResource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class RemoteDataSource {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    private val builder = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = builder.build()
    private val apiClient = retrofit.create(ApiService::class.java)

    companion object {

        @Volatile
        private var instance: RemoteDataSource? = null

        const val SHOW_MOVIE = "Movie"
        const val SHOW_TV = "Tv"

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource()
            }
    }

    fun getMovies(page: Int, callback: CustomCallback<ArrayList<Show>>) {
        EspressoIdlingResource.increment()
        val call = apiClient.showList(
            SHOW_MOVIE.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        )
        call.enqueue(object : Callback<ShowList> {
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

    fun getTvShows(page: Int, callback: CustomCallback<ArrayList<Show>>) {
        EspressoIdlingResource.increment()
        val call = apiClient.showList(
            SHOW_TV.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        )
        call.enqueue(object : Callback<ShowList>{
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

    fun getSearchedShowByQuery(
        category: String,
        page: Int,
        query: String,
        callback: CustomCallback<ArrayList<Show>>
    ) {
        EspressoIdlingResource.increment()
        val call = apiClient.search(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            query
        )
        call.enqueue(object : Callback<ShowList> {
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

    fun getSearchedShowByGenre(
        category: String,
        page: Int,
        genre: String,
        callback: CustomCallback<ArrayList<Show>>
    ) {
        EspressoIdlingResource.increment()
        val call = apiClient.showList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            genre
        )
        call.enqueue(object : Callback<ShowList> {
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
            MovieFragment.SHOW_MOVIE -> showId.let { apiClient.movie(it, BuildConfig.API_KEY) }
            else -> showId.let { apiClient.tv(it, BuildConfig.API_KEY) }
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
        val call = apiClient.getGenreList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY
        )
        call.enqueue(object : Callback<GenreList> {
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

}