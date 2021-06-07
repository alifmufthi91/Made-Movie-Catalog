package com.example.moviecatalogue.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.data.ShowRepository
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
    fun provideMovieViewModel(showRepository: ShowRepository): ViewModel {
        return MovieViewModel(showRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(TvViewModel::class)
    fun provideTvViewModel(showRepository: ShowRepository): ViewModel {
        return TvViewModel(showRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideSearchViewModel(showRepository: ShowRepository): ViewModel {
        return SearchViewModel(showRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(SearchByGenreViewModel::class)
    fun provideSearchByGenreViewModel(showRepository: ShowRepository): ViewModel {
        return SearchByGenreViewModel(showRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(DetailShowViewModel::class)
    fun provideDetailShowViewModel(showRepository: ShowRepository): ViewModel {
        return DetailShowViewModel(showRepository)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory {
        return viewModelFactory
    }
}