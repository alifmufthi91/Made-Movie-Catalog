package com.example.moviecatalogue.detail

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
import com.example.moviecatalogue.db.DatabaseContract
import com.example.moviecatalogue.helper.MappingHelper
import com.example.moviecatalogue.shows.movie.MovieFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailShowViewModel(
    private val movieCatalogueXRepository: MovieCatalogueXRepository

) : ViewModel() {

    private lateinit var context: Context
    private lateinit var show: Show
    private lateinit var type: String
    private var position: Int = 0
    internal var isFavourite = MutableLiveData<Boolean>(false)

    internal fun setShow(show: Show) {
        this.show = show
    }

    internal fun setType(type: String) {
        this.type = type
    }

    internal fun getShowInfo(): LiveData<Show> {
        val showId = show.movieDbId.toInt()
        return movieCatalogueXRepository.getShowDetail(type, showId)
    }

    fun setDetailData(context: Context, show: Show, type: String, position: Int) {
        this.context = context
        this.show = show
        this.type = type
        this.position = position
    }

    fun setFavorite(favouriteStatus: Boolean) {
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
            MovieFragment.SHOW_MOVIE -> Uri.parse("${DatabaseContract.FavoritesColumns.CONTENT_MOVIE_URI}/${show.movieDbId}")
            else -> Uri.parse("${DatabaseContract.FavoritesColumns.CONTENT_TV_URI}/${show.movieDbId}")
        }
        GlobalScope.launch(Dispatchers.Main) {
            val differedFavorite = async(Dispatchers.IO) {
                val cursor = context.contentResolver?.query(
                    uri,
                    null,
                    null,
                    null,
                    "${BaseColumns._ID} ASC"
                )
                cursor?.let { MappingHelper.isExist(it) }
            }
            val isShowFavorited = differedFavorite.await()
            Log.d("isFavorited?: ", isShowFavorited.toString())
            isShowFavorited?.let { setFavorite(it) }
        }
    }

    fun getShow() = show
    fun getType() = type
}