package com.example.moviecatalogue.provider


import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.moviecatalogue.db.DatabaseContract.AUTHORITY
import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_MOVIE_URI
import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_TV_URI
import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.TABLE_NAME
import com.example.moviecatalogue.db.FavouriteHelper
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue.shows.tv.TvFragment.Companion.SHOW_TV

class FavouriteProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_MOVIE = 1
        private const val FAVORITE_TV = 2
        private const val MOVIE_ID = 3
        private const val TV_ID = 4
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favouriteHelper: FavouriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$SHOW_MOVIE", FAVORITE_MOVIE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$SHOW_TV", FAVORITE_TV)
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/$SHOW_MOVIE/#",
                MOVIE_ID
            )
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/$SHOW_TV/#",
                TV_ID
            )
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var resolverUri: Uri = Uri.EMPTY
        var deleted = 0
        when (sUriMatcher.match(uri)) {
            MOVIE_ID -> {
                deleted = favouriteHelper.deleteById(uri.lastPathSegment.toString())
                resolverUri = CONTENT_MOVIE_URI
            }
            TV_ID -> {
                deleted = favouriteHelper.deleteById(uri.lastPathSegment.toString())
                resolverUri = CONTENT_TV_URI
            }
        }
        context?.contentResolver?.notifyChange(resolverUri, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var resolverUri: Uri = Uri.EMPTY
        var added: Long = 0
        when (sUriMatcher.match(uri)) {
            FAVORITE_MOVIE -> {
                added = favouriteHelper.insert(values)
                resolverUri = CONTENT_MOVIE_URI
            }
            FAVORITE_TV -> {
                added = favouriteHelper.insert(values)
                resolverUri = CONTENT_TV_URI
            }
        }

        context?.contentResolver?.notifyChange(resolverUri, null)
        return Uri.parse("$resolverUri/$added")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        favouriteHelper.open()
        Log.d("query", uri.toString())
        return when (sUriMatcher.match(uri)) {
            FAVORITE_MOVIE -> favouriteHelper.queryByShowType(SHOW_MOVIE)
            FAVORITE_TV -> favouriteHelper.queryByShowType(SHOW_TV)
            MOVIE_ID -> favouriteHelper.queryById(uri.lastPathSegment.toString())
            TV_ID -> favouriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun onCreate(): Boolean {
        favouriteHelper = FavouriteHelper.getInstance(context as Context)
        favouriteHelper.open()
        return true
    }


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
