package com.example.moviecatalogue.shows.tv

import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository

class TvViewModel(private val movieCatalogueXRepository: MovieCatalogueXRepository) : ViewModel() {
    private var currentPage = 1

    private fun setPage(page: Int) {
        currentPage = page
    }

    fun loadMore() {
        setPage(++currentPage)
        movieCatalogueXRepository.loadMoreTvShows(currentPage)
    }

    internal fun setShows() {
        movieCatalogueXRepository.setTvShows(currentPage)
    }

    internal fun getShows() = movieCatalogueXRepository.getTvShows()

}