package com.example.moviecatalogue.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.detail.DetailShowViewModel
import com.example.moviecatalogue.search.SearchByGenreViewModel
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.shows.movie.MovieViewModel
import com.example.moviecatalogue.shows.tv.TvViewModel
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(MovieViewModel::class)
    fun provideMovieViewModel(movieCatalogueXRepository: MovieCatalogueXRepository): ViewModel {
        return MovieViewModel(movieCatalogueXRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(TvViewModel::class)
    fun provideTvViewModel(movieCatalogueXRepository: MovieCatalogueXRepository): ViewModel {
        return TvViewModel(movieCatalogueXRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideSearchViewModel(movieCatalogueXRepository: MovieCatalogueXRepository): ViewModel {
        return SearchViewModel(movieCatalogueXRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SearchByGenreViewModel::class)
    fun provideSearchByGenreViewModel(movieCatalogueXRepository: MovieCatalogueXRepository): ViewModel {
        return SearchByGenreViewModel(movieCatalogueXRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailShowViewModel::class)
    fun provideDetailShowViewModel(movieCatalogueXRepository: MovieCatalogueXRepository): ViewModel {
        return DetailShowViewModel(movieCatalogueXRepository)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory {
        return viewModelFactory
    }
}