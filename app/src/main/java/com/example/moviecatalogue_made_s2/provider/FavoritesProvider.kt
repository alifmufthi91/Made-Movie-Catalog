package com.example.moviecatalogue_made_s2.provider


import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.moviecatalogue_made_s2.db.DatabaseContract.AUTHORITY
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_MOVIE_URI
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_TV_URI
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.SHOW_TYPE
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.TABLE_NAME
import com.example.moviecatalogue_made_s2.db.FavoritesHelper
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue_made_s2.ui.fragment.TvFragment.Companion.SHOW_TV

class FavoritesProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_Movie = 0
        private const val FAVORITE_Tv = 1
        private const val FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favsHelper: FavoritesHelper
        init {
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$SHOW_MOVIE", FAVORITE_Movie)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$SHOW_TV", FAVORITE_Tv)
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                FAVORITE_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favsHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_MOVIE_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (FAVORITE_ID) {
            sUriMatcher.match(uri) -> favsHelper.insert(values)
            else -> 0
        }
        val resolverUri = when (values?.get(SHOW_TYPE)){
            SHOW_MOVIE -> CONTENT_MOVIE_URI
            else -> CONTENT_TV_URI
        }
        context?.contentResolver?.notifyChange(resolverUri, null)
        return Uri.parse("$resolverUri/$added")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d("query", uri.toString())
        favsHelper.open()
        return when (sUriMatcher.match(uri)) {
            FAVORITE_Movie -> favsHelper.queryByShowType(SHOW_MOVIE)
            FAVORITE_Tv -> favsHelper.queryByShowType(SHOW_TV)
            FAVORITE_ID -> favsHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun onCreate(): Boolean {
        favsHelper = FavoritesHelper.getInstance(context as Context)
        favsHelper.open()
        return true
    }



    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}
