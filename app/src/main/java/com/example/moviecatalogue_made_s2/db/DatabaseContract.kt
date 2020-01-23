package com.example.moviecatalogue_made_s2.db

import android.provider.BaseColumns

object DatabaseContract {

    class FavoritesColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            const val _ID = "_id"
            const val MOVIEDB_ID = "moviedbid"
            const val NAME = "name"
            const val DESCRIPTION = "description"
            const val POSTER = "poster"
            const val SHOW_TYPE = "showType"
        }
    }
}