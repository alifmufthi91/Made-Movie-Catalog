package com.example.moviecatalogue.di

import com.example.moviecatalogue.about.AboutFragment
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.shows.tv.TvFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentProviderModule{
    @ContributesAndroidInjector
    abstract fun bindMovieFragment(): MovieFragment

    @ContributesAndroidInjector
    abstract fun bindTvShowFragment(): TvFragment

    @ContributesAndroidInjector
    abstract fun bindAboutFragment(): AboutFragment

}