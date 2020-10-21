package com.example.moviecatalogue.shows.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.BuildConfig
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

class MovieViewModel : ViewModel() {
    val movieShows = MutableLiveData<ArrayList<Show>>()


    internal fun setShows(page: Int) {
        val listShows = ArrayList<Show>()
        if (movieShows.value != null) {
            listShows.addAll(movieShows.value as ArrayList<Show>)
        }
        val builder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val movieDBClient = retrofit.create(MovieDB::class.java)
        val call = movieDBClient.showList(
            SHOW_MOVIE.toLowerCase(Locale.getDefault()),
            BuildConfig.API_KEY,
            page,
            null
        )
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                val showList = response.body()
                if (showList != null) {
                    listShows.addAll(showList.list)
                }
                movieShows.postValue(listShows)
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                Log.d("setShows()", "failed..")
            }

        })
    }

    internal fun getShows(): LiveData<ArrayList<Show>> = movieShows


}