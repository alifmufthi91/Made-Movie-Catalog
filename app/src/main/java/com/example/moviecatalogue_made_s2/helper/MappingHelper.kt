package com.example.favoritesmovie.helper

import android.database.Cursor
import com.example.moviecatalogue_made_s2.db.DatabaseContract
import com.example.moviecatalogue_made_s2.model.Show


object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor): ArrayList<Show> {
        cursor.moveToFirst()
        val arrayList = ArrayList<Show>()
        var show: Show
        if (cursor.count > 0) {
            do {
                show = Show()
                show.movieDbId =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.MOVIEDB_ID))
                        .toLong()
                show.name =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.NAME))
                show.overview =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.DESCRIPTION))
                show.imgPath =
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.POSTER))
                arrayList.add(show)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun mapCursorToObject(cursor: Cursor): Show {
        cursor.moveToFirst()
        val show = Show()
        if (cursor.count > 0) {
            show.movieDbId =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.MOVIEDB_ID))
                    .toLong()
            show.name =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.NAME))
            show.overview =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.DESCRIPTION))
            show.imgPath =
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.POSTER))
        }
        cursor.close()
        return show
    }

    fun isExist(cursor: Cursor): Boolean {
        cursor.moveToFirst()
        if (cursor.count > 0) {
            return true
        }
        cursor.close()
        return false
    }

}