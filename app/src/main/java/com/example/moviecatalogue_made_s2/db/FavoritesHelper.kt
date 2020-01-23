package com.example.moviecatalogue_made_s2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.DESCRIPTION
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.MOVIEDB_ID
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.NAME
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.POSTER
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.SHOW_TYPE
import com.example.moviecatalogue_made_s2.db.DatabaseContract.FavoritesColumns.Companion.TABLE_NAME
import com.example.moviecatalogue_made_s2.model.Show
import java.sql.SQLException

class FavoritesHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: FavoritesHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoritesHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = FavoritesHelper(context)
                    }
                }
            }
            return INSTANCE as FavoritesHelper
        }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun deleteById(id: String): Int {
        Log.d("delete movie id: ", id)
        return database.delete(DATABASE_TABLE, "$MOVIEDB_ID = '$id'", null)
    }

    fun getAllData(): ArrayList<Show> {
        val cursor =
            database.query(TABLE_NAME, null, null, null, null, null, "${BaseColumns._ID} ASC", null)
        cursor.moveToFirst()
        val arrayList = ArrayList<Show>()
        var show: Show
        if (cursor.count > 0) {
            do {
                show = Show()
                show.movieDbId = cursor.getString(cursor.getColumnIndexOrThrow(MOVIEDB_ID)).toLong()
                show.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                show.overview = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                show.imgPath = cursor.getString(cursor.getColumnIndexOrThrow(POSTER))
                arrayList.add(show)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun getDataByShowType(showType: String): ArrayList<Show> {
        val cursor = database.query(
            TABLE_NAME,
            null,
            "$SHOW_TYPE LIKE ?",
            arrayOf(showType),
            null,
            null,
            "${BaseColumns._ID} ASC",
            null
        )
        cursor.moveToFirst()
        val arrayList = ArrayList<Show>()
        var show: Show
        if (cursor.count > 0) {
            do {
                show = Show()
                show.movieDbId = cursor.getString(cursor.getColumnIndexOrThrow(MOVIEDB_ID)).toLong()
                show.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                show.overview = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                show.imgPath = cursor.getString(cursor.getColumnIndexOrThrow(POSTER))
                arrayList.add(show)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun isShowFavorited(id: String): Boolean {
        val cursor = database.query(
            TABLE_NAME,
            null,
            "$MOVIEDB_ID LIKE ?",
            arrayOf(id),
            null,
            null,
            "${BaseColumns._ID} ASC",
            null
        )
        cursor.moveToFirst()
        var show: Show
        if (cursor.count > 0) {
            do {
                show = Show()
                show.movieDbId = cursor.getString(cursor.getColumnIndexOrThrow(MOVIEDB_ID)).toLong()
                show.name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                Log.d("favorited", "${show.name} as  ${show.movieDbId}")
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        val isFavorited = cursor.count > 0
        cursor.close()
        return isFavorited
    }

    fun beginTransaction() {
        database.beginTransaction()
    }

    fun setTransactionSuccess() {
        database.setTransactionSuccessful()
    }

    fun endTransaction() {
        database.endTransaction()
    }

    fun insertTransaction(show: Show, showType: String) {
        val sql =
            ("INSERT INTO $TABLE_NAME ($NAME, $DESCRIPTION, $MOVIEDB_ID, $POSTER, $SHOW_TYPE) VALUES (?, ?, ?, ?, ?)")
        val stmt = database.compileStatement(sql)
        stmt.bindString(1, show.name)
        stmt.bindString(2, show.overview)
        stmt.bindString(3, show.movieDbId.toString())
        stmt.bindString(4, show.imgPath)
        stmt.bindString(5, showType)
        stmt.execute()
        stmt.clearBindings()
    }

}