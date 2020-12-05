package com.example.moviecatalogue.di

import com.example.moviecatalogue.detail.DetailShowActivity
import com.example.moviecatalogue.favourite.FavouriteActivity
import com.example.moviecatalogue.home.HomeActivity
import com.example.moviecatalogue.search.SearchActivity
import com.example.moviecatalogue.search.SearchByGenreActivity
import com.example.moviecatalogue.settings.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
    @ContributesAndroidInjector(modules = [(HomeFragmentProviderModule::class)])
    abstract fun bindMainActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun bindDetailShowActivity(): DetailShowActivity

    @ContributesAndroidInjector(modules = [(SearchFragmentProviderModule::class)])
    abstract fun bindSearchActivity(): SearchActivity

    @ContributesAndroidInjector
    abstract fun bindSearchActivityByGenre(): SearchByGenreActivity

    @ContributesAndroidInjector(modules = [(FavouriteFragmentProviderModule::class)])
    abstract fun bindFavouriteActivity(): FavouriteActivity

    @ContributesAndroidInjector
    abstract fun bindSettingsActivity(): SettingsActivity
}