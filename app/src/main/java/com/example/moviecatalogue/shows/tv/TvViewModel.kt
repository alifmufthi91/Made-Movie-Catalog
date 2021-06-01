package com.example.moviecatalogue.shows.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class TvViewModel @Inject constructor(val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModel() {
    private val currentPage = MutableLiveData<Int>(1)

    internal fun setPage(page: Int) {
        currentPage.value = page
    }

    val shows: LiveData<Resource<PagedList<ShowEntity>>> =
        Transformations.switchMap(currentPage) { currentPage ->
            setShows(currentPage)
            getShows()
        }

    private fun setShows(page: Int) {
        movieCatalogueXRepository.setTvShows(page)
//        if (page == 1) {
//            movieCatalogueXRepository.setTvShows(page)
//            return
//        }
//        movieCatalogueXRepository.loadMoreTvShows(page)
    }

    internal fun getShows(): LiveData<Resource<PagedList<ShowEntity>>> =
        movieCatalogueXRepository.getTvShows()

//    internal fun nextPage() {
//        currentPage.value = currentPage.value?.plus(1)
//    }

//    fun loadMore() {
//        setPage(++currentPage)
//        movieCatalogueXRepository.loadMoreTvShows(currentPage)
//    }
}