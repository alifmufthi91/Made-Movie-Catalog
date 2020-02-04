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

class ListViewModel : ViewModel() {
    val movieShows = MutableLiveData<ArrayList<Show>>()
    val tvShows = MutableLiveData<ArrayList<Show>>()


    internal fun setShows(category: String?, page: Int) {
        Log.d("setShows()", this.toString())
        val listShows= ArrayList<Show>()
        when (category){
            SHOW_MOVIE -> {
                if (movieShows.value != null){
                    listShows.addAll(movieShows.value as ArrayList<Show>)
                }
            }
            else -> {
                if (tvShows.value != null){
                    listShows.addAll(tvShows.value as ArrayList<Show>)
                }
            }
        }
        val builder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val movieDBClient = retrofit.create(MovieDB::class.java)
        Log.d("setShows()", "page : $page")
        val call = movieDBClient.showList(category?.toLowerCase(), BuildConfig.API_KEY, page)
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                val showList = response.body()
                if (showList != null) {
                    listShows.addAll(showList.list)
                }
                when (category) {
                    SHOW_MOVIE -> movieShows.postValue(listShows)
                    else -> tvShows.postValue(listShows)
                }
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                Log.d("setShows()", "failed..")
            }

        })
    }

    internal fun getShows(category: String?): LiveData<ArrayList<Show>> {
        return when (category) {
            SHOW_MOVIE -> movieShows
            else -> tvShows
        }
    }


}