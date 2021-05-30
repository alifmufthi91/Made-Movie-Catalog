package com.example.moviecatalogue.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.detail.DetailShowViewModel
import com.example.moviecatalogue.search.SearchByGenreViewModel
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.shows.movie.MovieViewModel
import com.example.moviecatalogue.shows.tv.TvViewModel
import javax.inject.Provider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        @Suppress("UNCHECKED_CAST")
        return creator.get() as T
    }
}
//
//class ViewModelFactory private constructor(private val movieCatalogueXRepository: MovieCatalogueXRepository) :
//    ViewModelProvider.NewInstanceFactory() {
//
//    companion object {
//        @Volatile
//        private var instance: ViewModelFactory? = null
//
//        fun getInstance(context: Context): ViewModelFactory =
//            instance ?: synchronized(this) {
//                instance ?: ViewModelFactory(Injection.provideRepository(context))
//            }
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        when {
//            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
//                return MovieViewModel(movieCatalogueXRepository) as T
//            }
//            modelClass.isAssignableFrom(TvViewModel::class.java) -> {
//                return TvViewModel(movieCatalogueXRepository) as T
//            }
//            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
//                return SearchViewModel(movieCatalogueXRepository) as T
//            }
//            modelClass.isAssignableFrom(SearchByGenreViewModel::class.java) -> {
//                return SearchByGenreViewModel(movieCatalogueXRepository) as T
//            }
//            modelClass.isAssignableFrom(DetailShowViewModel::class.java) -> {
//                return DetailShowViewModel(movieCatalogueXRepository) as T
//            }
//            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
//        }
//
//    }
//}