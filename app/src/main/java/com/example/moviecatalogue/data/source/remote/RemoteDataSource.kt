package com.example.moviecatalogue.data.source.remote

import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.data.model.GenreList
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.model.ShowList
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.utils.EspressoIdlingResource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

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

    fun getMovies(page: Int, callback: LoadMoviesCallback) {
        EspressoIdlingResource.increment()
        val call = apiClient.showList(
            SHOW_MOVIE.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        )
        call.enqueue(callback)
    }

    fun getTvShows(page: Int, callback: LoadTvShowsCallback) {
        EspressoIdlingResource.increment()
        val call = apiClient.showList(
            SHOW_TV.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        )
        call.enqueue(callback)
    }

    fun getSearchedShowByQuery(
        category: String,
        page: Int,
        query: String,
        callback: LoadSearchedShowCallback
    ) {
        EspressoIdlingResource.increment()
        val call = apiClient.search(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            query
        )
        call.enqueue(callback)
    }

    fun getSearchedShowByGenre(
        category: String,
        page: Int,
        genre: String,
        callback: LoadSearchedShowCallback
    ) {
        EspressoIdlingResource.increment()
        val call = apiClient.showList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            genre
        )
        call.enqueue(callback)
    }

    fun getShowDetail(type: String, showId: Int, callback: LoadShowDetailCallback) {
        EspressoIdlingResource.increment()
        val call = when (type) {
            MovieFragment.SHOW_MOVIE -> showId.let { apiClient.movie(it, BuildConfig.API_KEY) }
            else -> showId.let { apiClient.tv(it, BuildConfig.API_KEY) }
        }
        call.enqueue(callback)
    }

    fun getGenres(category: String, callback: LoadGenresCallback) {
        EspressoIdlingResource.increment()
        val call = apiClient.getGenreList(
            category.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY
        )
        call.enqueue(callback)
    }

    interface LoadMoviesCallback : Callback<ShowList>
    interface LoadTvShowsCallback : Callback<ShowList>
    interface LoadShowDetailCallback : Callback<Show>
    interface LoadSearchedShowCallback : Callback<ShowList>
    interface LoadGenresCallback : Callback<GenreList>

}