package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.ApiResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.vo.Resource

interface MovieCatalogueXDataSource {

    fun setMovies(page: Int)
    fun loadMoreMovies(page: Int): LiveData<ApiResponse<List<ShowResponse>>>
    fun setTvShows(page: Int)
    fun loadMoreTvShows(page: Int): LiveData<ApiResponse<List<ShowResponse>>>
    fun setSearchedShowsByQuery(category: String, page: Int, query: String)
    fun loadMoreSearchedShowsByQuery(category: String, page: Int, query: String): LiveData<ApiResponse<List<ShowResponse>>>
    fun setSearchedShowsByGenre(category: String, page: Int, genre: String)
    fun loadMoreSearchedShowsByGenre(category: String, page: Int, genre: String): LiveData<ApiResponse<List<ShowResponse>>>
    fun updateShow(show: ShowEntity)
    fun setGenres(category: String)

    fun getMovies(): LiveData<Resource<PagedList<ShowEntity>>>
    fun getTvShows(): LiveData<Resource<PagedList<ShowEntity>>>
    fun getShowDetail(type: String, showId: Long): LiveData<Resource<ShowEntity>>
    fun getSearchedShows(): LiveData<List<ShowEntity>>
    fun getGenres(): LiveData<List<GenreEntity>>
    fun getFavoriteMovies(): LiveData<PagedList<ShowEntity>>
    fun getFavoriteTvShows(): LiveData<PagedList<ShowEntity>>
}