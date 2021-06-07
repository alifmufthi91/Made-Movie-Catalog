package com.example.moviecatalogue.di

import android.content.Context
import com.example.moviecatalogue.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext(mainApplication: MainApplication): Context {
        return mainApplication
    }
}