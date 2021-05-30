package com.example.moviecatalogue.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.utils.AppExecutors
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import javax.inject.Inject

class SearchViewModel @Inject constructor(val movieCatalogueXRepository: MovieCatalogueXRepository) :
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

    fun loadMore() {
        setPage(++currentPage)
        movieCatalogueXRepository.loadMoreSearchedShowsByQuery(category, currentPage, query)
    }

    private fun setPage(page: Int) {
        currentPage = page
    }

    internal fun setGenres(){
        movieCatalogueXRepository.setGenres(getCategory())
    }

    internal fun getShows(): LiveData<List<ShowEntity>> =
        movieCatalogueXRepository.getSearchedShows()

    internal fun getGenres(): LiveData<List<GenreEntity>> = movieCatalogueXRepository.getGenres()

    internal fun getCategory() = category

    internal fun getQuery() = query
}