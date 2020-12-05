package com.example.moviecatalogue.di

import android.content.Context
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.room.MovieCatalogueXDatabase
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.utils.AppExecutors
import javax.inject.Inject

//object Injection {
//    fun provideRepository(context: Context): MovieCatalogueXRepository {
//
//        val database = MovieCatalogueXDatabase.getInstance(context)
//        val remoteDataSource = RemoteDataSource()
//        val localDataSource = LocalDataSource.getInstance(database.movieCatalogueXDao())
//        val appExecutors = AppExecutors()
//
//        return MovieCatalogueXRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
//    }
//}