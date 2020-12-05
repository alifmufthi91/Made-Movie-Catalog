package com.example.moviecatalogue.di

import android.content.Context
import androidx.room.Room
import com.example.moviecatalogue.data.source.local.room.MovieCatalogueXDao
import com.example.moviecatalogue.data.source.local.room.MovieCatalogueXDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MovieCatalogueXDatabase{
        return Room.databaseBuilder(context.applicationContext,
            MovieCatalogueXDatabase::class.java,
            "MovieCatalogueX.db").build()
    }

    @Provides
    @Singleton
    fun provideDao(movieCatalogueXDatabase: MovieCatalogueXDatabase): MovieCatalogueXDao{
        return movieCatalogueXDatabase.movieCatalogueXDao()
    }


}