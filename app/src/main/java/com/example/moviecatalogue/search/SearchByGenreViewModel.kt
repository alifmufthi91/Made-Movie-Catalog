package com.example.moviecatalogue.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import javax.inject.Inject

class SearchByGenreViewModel @Inject constructor(val showRepository: ShowRepository) :
    ViewModel() {
    private var category = SHOW_MOVIE
    private var currentPage = 1
    private lateinit var selectedGenre: String

    internal fun setShows(category: String) {
        showRepository.setSearchedShowsByGenre(category, currentPage, selectedGenre)
    }

    internal fun setGenre(genre: String) {
        selectedGenre = genre
    }

    internal fun setCategory(category: String) {
        this.category = category
    }

    internal fun getShows(): LiveData<List<ShowEntity>> =
        showRepository.getSearchedShows()

    internal fun getCategory(): String = category

//    internal fun getGenre() = selectedGenre
//    internal fun loadMore() {
//        setPage(++currentPage)
//        movieCatalogueXRepository.loadMoreSearchedShowsByGenre(category, currentPage, selectedGenre)
//    }
//
//    private fun setPage(page: Int) {
//        currentPage = page
//    }
}