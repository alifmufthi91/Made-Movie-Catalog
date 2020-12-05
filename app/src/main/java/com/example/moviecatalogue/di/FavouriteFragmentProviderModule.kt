package com.example.moviecatalogue.di

import com.example.moviecatalogue.favourite.movie.FavoriteMovieFragment
import com.example.moviecatalogue.favourite.tv.FavoriteTvFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FavouriteFragmentProviderModule {
    @ContributesAndroidInjector
    abstract fun bindFavouriteMovieFragment(): FavoriteMovieFragment

    @ContributesAndroidInjector
    abstract fun bindFavouriteTvFragment(): FavoriteTvFragment
}