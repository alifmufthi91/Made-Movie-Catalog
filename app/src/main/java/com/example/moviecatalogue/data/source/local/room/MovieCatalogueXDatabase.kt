package com.example.moviecatalogue.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.utils.Converters

@Database(entities = [ShowEntity::class, GenreEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieCatalogueXDatabase : RoomDatabase() {
    abstract fun movieCatalogueXDao(): MovieCatalogueXDao

    companion object {

//        @Volatile
//        private var INSTANCE: MovieCatalogueXDatabase? = null
//
//        fun getInstance(context: Context): MovieCatalogueXDatabase =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?:
//            }
    }
}