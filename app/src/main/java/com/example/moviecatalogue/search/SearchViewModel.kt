package com.example.moviecatalogue.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import javax.inject.Inject

class SearchViewModel @Inject constructor(val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModel() {
    private var category = SHOW_MOVIE
    private var currentPage = 1
    private val query = MutableLiveData<String>("")

    private fun setShows(category: String, query: String) {
        movieCatalogueXRepository.setSearchedShowsByQuery(category, currentPage, query)
    }

    internal fun setCategory(category: String) {
        this.category = category
    }

    internal fun setQuery(query: String) {
        this.query.value = query
    }

    private fun setPage(page: Int) {
        currentPage = page
    }

    internal fun setGenres() {
        movieCatalogueXRepository.setGenres(getCategory())
    }

    var searchedShows: LiveData<List<ShowEntity>> = Transformations.switchMap(query) { query ->
        setShows(category, query)
        getShows()
    }

    internal fun getShows(): LiveData<List<ShowEntity>> =
        movieCatalogueXRepository.getSearchedShows()

    internal fun getGenres(): LiveData<List<GenreEntity>> = movieCatalogueXRepository.getGenres()

    internal fun getCategory() = category

//        fun loadMore() {
//        setPage(++currentPage)
//        movieCatalogueXRepository.loadMoreSearchedShowsByQuery(
//            category, currentPage,
//            query.value.toString()
//        )
//    }
}