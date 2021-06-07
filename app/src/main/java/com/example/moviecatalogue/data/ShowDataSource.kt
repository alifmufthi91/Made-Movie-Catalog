package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource

interface ShowDataSource {

    fun setMovies(page: Int)

    fun setTvShows(page: Int)

    fun setSearchedShowsByQuery(category: String, page: Int, query: String)
    fun setSearchedShowsByGenre(category: String, page: Int, genre: String)
    fun updateShow(show: ShowEntity)
    fun setGenres(category: String)

    fun getMovies(): LiveData<Resource<PagedList<ShowEntity>>>
    fun getTvShows(): LiveData<Resource<PagedList<ShowEntity>>>
    fun getShowDetail(type: String, showId: Long): LiveData<Resource<ShowEntity>>
    fun getSearchedShows(): LiveData<List<ShowEntity>>
    fun getGenres(): LiveData<List<GenreEntity>>
    fun getFavoriteMovies(): LiveData<PagedList<ShowEntity>>
    fun getFavoriteTvShows(): LiveData<PagedList<ShowEntity>>

    //    fun loadMoreMovies(page: Int)
    //    fun loadMoreTvShows(page: Int)
//    fun loadMoreSearchedShowsByQuery(
//        category: String,
//        page: Int,
//        query: String
//    ): LiveData<ApiResponse<List<ShowResponse>>>
//    fun loadMoreSearchedShowsByGenre(
//        category: String,
//        page: Int,
//        genre: String
//    ): LiveData<ApiResponse<List<ShowResponse>>>

}