package com.example.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.Show

interface MovieCatalogueXDataSource {

    fun setMovies(page: Int)
    fun loadMoreMovies(page: Int)
    fun setTvShows(page: Int)
    fun loadMoreTvShows(page: Int)
    fun setSearchedShowsByQuery(category: String, page: Int, query: String)
    fun loadMoreSearchedShowsByQuery(category: String, page: Int, query: String)
    fun setSearchedShowsByGenre(category: String, page: Int, genre: String)
    fun loadMoreSearchedShowsByGenre(category: String, page: Int, genre: String)
    fun setGenres(category: String)

    fun getMovies(): LiveData<ArrayList<Show>>
    fun getTvShows(): LiveData<ArrayList<Show>>
    fun getShowDetail(type: String, showId: Int): LiveData<Show>
    fun getSearchedShows(): LiveData<ArrayList<Show>>
    fun getGenres(): LiveData<ArrayList<Genre>>
}