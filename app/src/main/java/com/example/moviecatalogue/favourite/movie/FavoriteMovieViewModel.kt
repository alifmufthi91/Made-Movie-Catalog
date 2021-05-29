package com.example.moviecatalogue.favourite.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class FavoriteMovieViewModel @Inject constructor(val movieCatalogueXRepository: MovieCatalogueXRepository) :
    ViewModel() {

    internal fun getFavoriteMovies(): LiveData<PagedList<ShowEntity>> =
        movieCatalogueXRepository.getFavoriteMovies()
}