package com.example.moviecatalogue.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE

class SearchViewModel(private val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModel() {
    private var category = SHOW_MOVIE
    private var currentPage = 1
    private var query = ""

    internal fun setShows(category: String, query: String) {
        movieCatalogueXRepository.setSearchedShowsByQuery(category, currentPage, query)
    }

    internal fun setCategory(category: String) {
        this.category = category
    }

    internal fun setQuery(query: String) {
        this.query = query
    }

    internal fun setGenres() {
        movieCatalogueXRepository.setGenres(category)
    }

    fun loadMore() {
        setPage(++currentPage)
        movieCatalogueXRepository.loadMoreSearchedShowsByQuery(category, currentPage, query)
    }

    private fun setPage(page: Int) {
        currentPage = page
    }

    internal fun getShows(): LiveData<ArrayList<Show>> =
        movieCatalogueXRepository.getSearchedShows()

    internal fun getGenres(): LiveData<ArrayList<Genre>> = movieCatalogueXRepository.getGenres()

    internal fun getCategory() = category

    internal fun getQuery() = query
}