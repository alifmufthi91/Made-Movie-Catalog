package com.example.moviecatalogue.di

import android.content.Context
import androidx.room.Room
import com.example.moviecatalogue.data.source.local.room.ShowDao
import com.example.moviecatalogue.data.source.local.room.ShowDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): ShowDatabase{
        return Room.databaseBuilder(context.applicationContext,
            ShowDatabase::class.java,
            "MovieCatalogueX.db").build()
    }

    @Provides
    @Singleton
    fun provideDao(movieCatalogueXDatabase: ShowDatabase): ShowDao{
        return movieCatalogueXDatabase.movieCatalogueXDao()
    }


}