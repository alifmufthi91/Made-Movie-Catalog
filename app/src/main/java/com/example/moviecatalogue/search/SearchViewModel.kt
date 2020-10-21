package com.example.moviecatalogue.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.model.Genre
import com.example.moviecatalogue.model.GenreList
import com.example.moviecatalogue.model.Show
import com.example.moviecatalogue.model.ShowList
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue.utils.MovieDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class SearchViewModel : ViewModel() {
    val searchResults = MutableLiveData<ArrayList<Show>>()
    var category = SHOW_MOVIE
    var currentPage = 1
    var query = ""
    val genreResult = MutableLiveData<ArrayList<Genre>>()
    val listGenre = ArrayList<Genre>()
    var totalResult = 0


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val movieDBClient = retrofit.create(MovieDB::class.java)

    companion object {
        const val FIRST_PAGE = 1
    }


    internal fun setShows(category: String?, page: Int, query: String) {
        Log.d("setShows()", this.toString())
        val listShows = ArrayList<Show>()
        Log.d("setShows()", "page : $page")
        val call = movieDBClient.search(
            category?.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            query
        )
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                val showList = response.body()
                if (showList != null) {
                    listShows.addAll(showList.list)
                    totalResult = showList.total
                }
                searchResults.postValue(listShows)
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                Log.d("setShows()", "failed..")
            }

        })
    }


    internal fun loadMore(category: String?, page: Int, query: String) {
        Log.d("loadMore()", this.toString())
        val listShows = ArrayList<Show>()
        if (searchResults.value != null) {
            listShows.addAll(searchResults.value as ArrayList<Show>)
        }
        Log.d("loadMore()", "page : $page")
        val call = movieDBClient.search(
            category?.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            query
        )
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                val showList = response.body()
                if (showList != null) {
                    listShows.addAll(showList.list)
                }
                searchResults.postValue(listShows)
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                Log.d("setShows()", "failed..")
            }

        })
    }

    internal fun getShows(): LiveData<ArrayList<Show>> {
        return searchResults
    }

    internal fun setGenres(category: String?) {
        listGenre.clear()
        val call = movieDBClient.getGenreList(
            category?.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY
        )
        call.enqueue(object : Callback<GenreList> {
            override fun onResponse(call: Call<GenreList>, response: Response<GenreList>) {
                val genreList = response.body()
                if (genreList != null) {
                    listGenre.addAll(genreList.list)
                }
                genreResult.postValue(listGenre)
            }

            override fun onFailure(call: Call<GenreList>, t: Throwable) {
                Log.d("getGenre()", "failed..")
            }
        })
    }

    internal fun getGenre(): LiveData<ArrayList<Genre>> {
        return genreResult
    }
}