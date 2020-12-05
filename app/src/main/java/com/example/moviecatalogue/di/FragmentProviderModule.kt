package com.example.moviecatalogue.di

import com.example.moviecatalogue.about.AboutFragment
import com.example.moviecatalogue.favourite.movie.FavoriteMovieFragment
import com.example.moviecatalogue.favourite.tv.FavoriteTvFragment
import com.example.moviecatalogue.search.menu.SearchMenuFragment
import com.example.moviecatalogue.search.result.SearchResultFragment
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.shows.tv.TvFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentProviderModule {
    @ContributesAndroidInjector
    abstract fun bindMovieFragment(): MovieFragment

    @ContributesAndroidInjector
    abstract fun bindTvShowFragment(): TvFragment

    @ContributesAndroidInjector
    abstract fun bindSearchMenuFragment(): SearchMenuFragment

    @ContributesAndroidInjector
    abstract fun bindSearchResultFragment(): SearchResultFragment

    @ContributesAndroidInjector
    abstract fun bindFavouriteMovieFragment(): FavoriteMovieFragment

    @ContributesAndroidInjector
    abstract fun bindFavouriteTvFragment(): FavoriteTvFragment

    @ContributesAndroidInjector
    abstract fun bindAboutFragment(): AboutFragment

}