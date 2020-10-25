package com.example.moviecatalogue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
import com.example.moviecatalogue.detail.DetailShowViewModel
import com.example.moviecatalogue.di.Injection
import com.example.moviecatalogue.search.SearchByGenreViewModel
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.shows.movie.MovieViewModel
import com.example.moviecatalogue.shows.tv.TvViewModel

class ViewModelFactory private constructor(private val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                return MovieViewModel(movieCatalogueXRepository) as T
            }
            modelClass.isAssignableFrom(TvViewModel::class.java) -> {
                return TvViewModel(movieCatalogueXRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                return SearchViewModel(movieCatalogueXRepository) as T
            }
            modelClass.isAssignableFrom(SearchByGenreViewModel::class.java) -> {
                return SearchByGenreViewModel(movieCatalogueXRepository) as T
            }
            modelClass.isAssignableFrom(DetailShowViewModel::class.java) -> {
                return DetailShowViewModel(movieCatalogueXRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    }
}