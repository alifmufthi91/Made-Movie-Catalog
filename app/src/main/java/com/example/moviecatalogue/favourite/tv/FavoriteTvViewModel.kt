package com.example.moviecatalogue.favourite.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import javax.inject.Inject

class FavoriteTvViewModel @Inject constructor(val showRepository: ShowRepository) :
    ViewModel() {

    internal fun getFavoriteTvShows(): LiveData<PagedList<ShowEntity>> =
        showRepository.getFavoriteTvShows()
}