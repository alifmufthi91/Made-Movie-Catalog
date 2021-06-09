package com.example.moviecatalogue.shows.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class MovieViewModel @Inject constructor(val showRepository: ShowRepository) :
    ViewModel() {

    private val currentPage = MutableLiveData(1)

    internal fun setPage(page: Int) {
        currentPage.value = page
    }


    val shows: LiveData<Resource<PagedList<ShowEntity>>> =
        Transformations.switchMap(currentPage) { currentPage ->
            setShows(currentPage)
            getShows()
        }

    private fun setShows(page: Int) {
        showRepository.setMovies(page)
//        if (page == 1) {
//            movieCatalogueXRepository.setMovies(page)
//            return
//        }
//        movieCatalogueXRepository.loadMoreMovies(page)
    }

    internal fun getShows(): LiveData<Resource<PagedList<ShowEntity>>> =
        showRepository.getMovies()

//    fun loadMore() {
//        setPage(++currentPage)
//        movieCatalogueXRepository.loadMoreMovies(currentPage)
//    }
//    internal fun nextPage() {
//        currentPage.value = currentPage.value?.plus(1)
//    }
}