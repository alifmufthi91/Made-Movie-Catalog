package com.example.favoritesmovie.db

import android.net.Uri
import android.provider.BaseColumns
import com.example.favoritesmovie.ui.fragment.FavoriteMovieFragment.Companion.SHOW_MOVIE
import com.example.favoritesmovie.ui.fragment.FavoriteTvFragment.Companion.SHOW_TV

object DatabaseContract {

    const val AUTHORITY = "com.example.moviecatalogue_made_s2"
    const val SCHEME = "content"

    class FavoritesColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            const val _ID = "_id"
            const val MOVIEDB_ID = "moviedbid"
            const val NAME = "name"
            const val DESCRIPTION = "description"
            const val POSTER = "poster"
            const val SHOW_TYPE = "showType"

            val CONTENT_MOVIE_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .appendPath(SHOW_MOVIE)
                .build()

            val CONTENT_TV_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .appendPath(SHOW_TV)
                .build()
        }
    }
}