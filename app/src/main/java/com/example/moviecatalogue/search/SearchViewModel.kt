package com.example.moviecatalogue.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import javax.inject.Inject

class SearchViewModel @Inject constructor(val showRepository: ShowRepository) :
    ViewModel() {
    private var category = SHOW_MOVIE
    private var currentPage = 1
    private val query = MutableLiveData("")

    private fun setShows(category: String, query: String) {
        showRepository.setSearchedShowsByQuery(category, currentPage, query)
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
        showRepository.setGenres(getCategory())
    }

    var searchedShows: LiveData<List<ShowEntity>> = Transformations.switchMap(query) { query ->
        setShows(category, query)
        getShows()
    }

    internal fun getShows(): LiveData<List<ShowEntity>> =
        showRepository.getSearchedShows()

    internal fun getGenres(): LiveData<List<GenreEntity>> = showRepository.getGenres()

    internal fun getCategory() = category

//        fun loadMore() {
//        setPage(++currentPage)
//        movieCatalogueXRepository.loadMoreSearchedShowsByQuery(
//            category, currentPage,
//            query.value.toString()
//        )
//    }
}