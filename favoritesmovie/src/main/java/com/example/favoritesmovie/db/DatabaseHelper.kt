package com.example.favoritesmovie.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.favoritesmovie.db.DatabaseContract.FavoritesColumns.Companion.TABLE_NAME


internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbmoviecatalog"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.FavoritesColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavoritesColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavoritesColumns.DESCRIPTION} TEXT NOT NULL," +
                " ${DatabaseContract.FavoritesColumns.SHOW_TYPE} TEXT NOT NULL," +
                " ${DatabaseContract.FavoritesColumns.MOVIEDB_ID} TEXT NOT NULL," +
                " ${DatabaseContract.FavoritesColumns.POSTER} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, newVersion: Int, oldVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}