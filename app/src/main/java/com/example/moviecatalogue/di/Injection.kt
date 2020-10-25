package com.example.moviecatalogue.di

import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.remote.RemoteDataSource

object Injection {
    fun provideRepository(): MovieCatalogueXRepository {

        val remoteDataSource = RemoteDataSource.getInstance()

        return MovieCatalogueXRepository.getInstance(remoteDataSource)
    }
}