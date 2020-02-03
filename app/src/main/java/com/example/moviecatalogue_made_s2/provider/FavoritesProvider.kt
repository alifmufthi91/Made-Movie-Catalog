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
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.TABLE_NAME
import com.example.moviecatalogue_made_s2.db.FavoritesHelper
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue_made_s2.ui.fragment.TvFragment.Companion.SHOW_TV

class FavoritesProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_MOVIE = 1
        private const val FAVORITE_TV = 2
        private const val MOVIE_ID = 3
        private const val TV_ID = 4
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favsHelper: FavoritesHelper
        init {
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$SHOW_MOVIE", FAVORITE_MOVIE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$SHOW_TV", FAVORITE_TV)
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/$SHOW_MOVIE/#",
                MOVIE_ID)
            sUriMatcher.addURI(AUTHORITY,
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
                deleted = favsHelper.deleteById(uri.lastPathSegment.toString())
                resolverUri = CONTENT_MOVIE_URI
            }
            TV_ID -> {
                deleted = favsHelper.deleteById(uri.lastPathSegment.toString())
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
                added = favsHelper.insert(values)
                resolverUri = CONTENT_MOVIE_URI
            }
            FAVORITE_TV -> {
                added = favsHelper.insert(values)
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
        favsHelper.open()
        Log.d("query", uri.toString())
        return when (sUriMatcher.match(uri)) {
            FAVORITE_MOVIE -> favsHelper.queryByShowType(SHOW_MOVIE)
            FAVORITE_TV -> favsHelper.queryByShowType(SHOW_TV)
            MOVIE_ID -> favsHelper.queryById(uri.lastPathSegment.toString())
            TV_ID -> favsHelper.queryById(uri.lastPathSegment.toString())
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
