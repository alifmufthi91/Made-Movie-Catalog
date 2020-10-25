package com.example.moviecatalogue.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE

class SearchByGenreViewModel(private val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModel() {
    private var category = SHOW_MOVIE
    private var currentPage = 1
    private lateinit var selectedGenre: String

    internal fun setShows(category: String) {
        movieCatalogueXRepository.setSearchedShowsByGenre(category, currentPage, selectedGenre)
    }


    internal fun loadMore() {
        setPage(++currentPage)
        movieCatalogueXRepository.loadMoreSearchedShowsByGenre(category, currentPage, selectedGenre)
    }

    private fun setPage(page: Int) {
        currentPage = page
    }

    internal fun setGenre(genre: String) {
        selectedGenre = genre
    }

    internal fun setCategory(category: String) {
        this.category = category
    }

    internal fun getShows(): LiveData<ArrayList<Show>> =
        movieCatalogueXRepository.getSearchedShows()

    internal fun getGenre() = selectedGenre

    internal fun getCategory(): String = category

}