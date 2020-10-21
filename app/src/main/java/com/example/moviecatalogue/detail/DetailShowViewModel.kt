package com.example.moviecatalogue.detail

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.db.DatabaseContract
import com.example.moviecatalogue.helper.MappingHelper
import com.example.moviecatalogue.model.Show
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.utils.MovieDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailShowViewModel(
    private val context: Context,
    internal val show: Show,
    internal val type: String,
    internal val position: Int
) : ViewModel() {

    internal var showLiveData = MutableLiveData<Show>(show)
    internal var isFavourite = MutableLiveData<Boolean>(false)

    internal fun getShowInfo() {
        Log.d("getShowInfo()", this.toString())
        val builder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val movieDBClient = retrofit.create(MovieDB::class.java)
        val showId = showLiveData.value?.movieDbId?.toInt()
        val call = when (type) {
            MovieFragment.SHOW_MOVIE -> showId?.let { movieDBClient.movie(it, BuildConfig.API_KEY) }
            else -> showId?.let { movieDBClient.tv(it, BuildConfig.API_KEY) }
        }
        call?.enqueue(object : Callback<Show> {
            override fun onResponse(call: Call<Show>, response: Response<Show>?) {
                showLiveData.postValue(response?.body())
                Log.d("getShowInfo : ", "Success.. ${showLiveData.value?.vote_average}")
            }

            override fun onFailure(call: Call<Show>, t: Throwable) {
                Log.d("getShowInfo : ", " Failed..")
            }
        })

    }

    fun setFavorite(favouriteStatus: Boolean){
        isFavourite.postValue(favouriteStatus)
    }

    internal fun setValues(show: Show, showType: String): ContentValues {
        val values = ContentValues()
        values.put(DatabaseContract.FavoritesColumns.NAME, show.name)
        values.put(DatabaseContract.FavoritesColumns.DESCRIPTION, show.overview)
        values.put(DatabaseContract.FavoritesColumns.MOVIEDB_ID, show.movieDbId.toString())
        values.put(DatabaseContract.FavoritesColumns.POSTER, show.imgPath)
        values.put(DatabaseContract.FavoritesColumns.SHOW_TYPE, showType)
        return values
    }

    internal fun getFavouriteStatus() {
        val uri = when (type) {
            MovieFragment.SHOW_MOVIE -> Uri.parse("${DatabaseContract.FavoritesColumns.CONTENT_MOVIE_URI}/${showLiveData.value?.movieDbId.toString()}")
            else -> Uri.parse("${DatabaseContract.FavoritesColumns.CONTENT_TV_URI}/${showLiveData.value?.movieDbId.toString()}")
        }
        GlobalScope.launch(Dispatchers.Main) {
            val differedFavorite = async(Dispatchers.IO) {

                val cursor = context.contentResolver?.query(
                    uri,
                    null,
                    null,
                    null,
                    "${BaseColumns._ID} ASC"
                ) as Cursor
                MappingHelper.isExist(cursor)
            }
            val isShowFavorited = differedFavorite.await()
            Log.d("isFavorited?: ", isShowFavorited.toString())
            setFavorite(isShowFavorited)
        }
    }


}