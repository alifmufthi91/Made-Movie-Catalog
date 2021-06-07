package com.example.moviecatalogue.di

import com.example.moviecatalogue.search.menu.SearchMenuFragment
import com.example.moviecatalogue.search.result.SearchResultFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchFragmentProviderModule {
    @ContributesAndroidInjector
    abstract fun bindSearchMenuFragment(): SearchMenuFragment

    @ContributesAndroidInjector
    abstract fun bindSearchResultFragment(): SearchResultFragment

}