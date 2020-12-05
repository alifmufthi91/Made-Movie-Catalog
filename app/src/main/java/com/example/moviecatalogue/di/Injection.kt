package com.example.moviecatalogue.di

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