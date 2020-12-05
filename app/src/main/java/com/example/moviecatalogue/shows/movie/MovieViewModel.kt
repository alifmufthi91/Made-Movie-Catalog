package com.example.moviecatalogue.shows.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class MovieViewModel @Inject constructor(val movieCatalogueXRepository: MovieCatalogueXRepository) :
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

    internal fun getShows(): LiveData<Resource<PagedList<ShowEntity>>> =
        movieCatalogueXRepository.getMovies()
}