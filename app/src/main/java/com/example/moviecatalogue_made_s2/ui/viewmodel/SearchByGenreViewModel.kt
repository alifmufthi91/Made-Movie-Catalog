package com.example.moviecatalogue_made_s2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue_made_s2.BuildConfig
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.model.ShowList
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue_made_s2.utils.MovieDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class SearchByGenreViewModel : ViewModel() {
    val searchResults = MutableLiveData<ArrayList<Show>>()
    var category = SHOW_MOVIE
    var currentPage = 1
    var totalResults = 0



    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val movieDBClient = retrofit.create(MovieDB::class.java)

    companion object{
        const val FIRST_PAGE = 1
    }


    internal fun setShows(category: String?, page: Int, genre: String) {
        Log.d("setShows()", this.toString())
        val listShows= ArrayList<Show>()
        Log.d("setShows()", "page : $page")
        val call = movieDBClient.showList(category?.toLowerCase(Locale.getDefault()), BuildConfig.API_KEY, page, genre)
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                val showList = response.body()
                if (showList != null) {
                    listShows.addAll(showList.list)
                    totalResults = showList.total
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
        val listShows= ArrayList<Show>()
        if (searchResults.value != null){
            listShows.addAll(searchResults.value as ArrayList<Show>)
        }
        Log.d("loadMore()", "page : $page")
        val call = movieDBClient.showList(category?.toLowerCase(Locale.getDefault()), BuildConfig.API_KEY, page, query)
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


}