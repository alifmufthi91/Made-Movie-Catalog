package com.example.moviecatalogue.shows.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository

class MovieViewModel(private val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModel() {

    private var currentPage = 1

    private fun setPage(page: Int) {
        currentPage = page
    }

    fun loadMore() {
        setPage(++currentPage)
        movieCatalogueXRepository.loadMoreMovies(currentPage)
    }

    internal fun setShows() {
        movieCatalogueXRepository.setMovies(currentPage)
    }

    internal fun getShows(): LiveData<ArrayList<Show>> = movieCatalogueXRepository.getMovies()


}