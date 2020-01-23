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


    internal fun setShows(category: String?) {
        Log.d("setShows()", this.toString())
        val showList = ArrayList<Show>()
        val builder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val movieDBClient = retrofit.create(MovieDB::class.java)
        val call = movieDBClient.showList(category?.toLowerCase(), BuildConfig.API_KEY)
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                val shows = response.body()
                if (shows != null) {
                    showList.addAll(shows.list)
                }
                showList.forEach { (index) -> Log.d("title $category", index.toString()) }
                when (category) {
                    SHOW_MOVIE -> movieShows.postValue(showList)
                    else -> tvShows.postValue(showList)
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