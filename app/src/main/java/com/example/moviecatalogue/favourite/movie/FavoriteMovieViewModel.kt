package com.example.moviecatalogue.favourite.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(val showRepository: ShowRepository) :
    ViewModel() {

    internal fun getFavoriteMovies(): LiveData<PagedList<ShowEntity>> =
        showRepository.getFavoriteMovies()
}