package com.example.favoritesmovie.helper

import android.database.Cursor
import com.example.favoritesmovie.db.DatabaseContract
import com.example.favoritesmovie.model.Show

object MappingHelper {

    fun mapCursorToArrayList(cursor: Cursor): ArrayList<Show> {
        cursor.moveToFirst()
        val arrayList = ArrayList<Show>()
        var show: Show
        if (cursor.count > 0) {
            do {
                show = Show()
                show.movieDbId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.MOVIEDB_ID)).toLong()
                show.name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.NAME))
                show.overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.DESCRIPTION))
                show.imgPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.POSTER))
                arrayList.add(show)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun isExist(cursor: Cursor): Boolean {
        cursor.moveToFirst()
        if(cursor.count > 0){
            return true
        }
        cursor.close()
        return false
    }

}