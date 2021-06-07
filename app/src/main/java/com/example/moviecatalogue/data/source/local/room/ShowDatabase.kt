package com.example.moviecatalogue.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.utils.Converters

@Database(
    entities = [ShowEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShowDatabase : RoomDatabase() {
    abstract fun movieCatalogueXDao(): ShowDao

    companion object {
        @Volatile
        private var INSTANCE: ShowDatabase? = null

        fun getInstance(context: Context): ShowDatabase {
            if (INSTANCE == null) {
                synchronized(ShowDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ShowDatabase::class.java, "show_database"
                    )
                        .build()
                }
            }
            return INSTANCE as ShowDatabase
        }

    }
}