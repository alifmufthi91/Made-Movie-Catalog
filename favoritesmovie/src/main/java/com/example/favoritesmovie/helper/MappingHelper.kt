package com.example.favoritesmovie.helper

import android.database.Cursor
import com.example.favoritesmovie.db.DatabaseContract
import com.example.favoritesmovie.model.Show

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor): ArrayList<Show> {
        val notesList = ArrayList<Show>()
        notesCursor.moveToFirst()
        while (notesCursor.moveToNext()) {
            val title =
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.NAME))
            val description =
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.DESCRIPTION))
            val moviedbId =
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.MOVIEDB_ID))
            val poster =
                notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.POSTER))
            notesList.add(Show(title, 0f, "", poster, description, 0f, 0, moviedbId.toLong()))
        }
        return notesList
    }

    fun mapCursorToObject(notesCursor: Cursor): Show {
        notesCursor.moveToFirst()
        val title =
            notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.NAME))
        val description =
            notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.DESCRIPTION))
        val moviedbId =
            notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.MOVIEDB_ID))
        val poster =
            notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.FavoritesColumns.POSTER))
        return Show(title, 0f, "", poster, description, 0f, 0, moviedbId.toLong())
    }
}