package com.example.moviecatalogue.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(private val viewModelMap: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        viewModelMap[modelClass]?.get() as T
}

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