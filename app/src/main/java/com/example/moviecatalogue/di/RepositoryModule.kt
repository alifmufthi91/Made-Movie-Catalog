package com.example.moviecatalogue.di

import com.example.moviecatalogue.data.source.local.room.MovieCatalogueXDatabase
import com.example.moviecatalogue.utils.DiskIOThreadExecutor
import com.example.moviecatalogue.utils.MainThreadExecutor
import com.example.moviecatalogue.utils.NetworkIOThreadExecutor
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named("diskIoExecutor")
    fun provideDiskIO(diskIOExecutor: DiskIOThreadExecutor): Executor{
        return diskIOExecutor
    }

    @Provides
    @Singleton
    @Named("networkIoExecutor")
    fun provideNetworkIO(networkIOThreadExecutor: NetworkIOThreadExecutor): Executor{
        return networkIOThreadExecutor
    }

    @Provides
    @Singleton
    @Named("mainThreadExecutor")
    fun provideMainThread(mainThreadExecutor: MainThreadExecutor): Executor{
        return mainThreadExecutor
    }
}