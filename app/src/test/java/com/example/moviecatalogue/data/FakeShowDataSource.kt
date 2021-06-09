package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource

interface FakeShowDataSource {
    fun getMovies(page: Int): LiveData<Resource<PagedList<ShowEntity>>>
    fun getTvShows(page: Int): LiveData<Resource<PagedList<ShowEntity>>>
    fun getShowDetail(type: String, showId: Long): LiveData<Resource<ShowEntity>>
    fun getFavoriteMovies(): LiveData<PagedList<ShowEntity>>
    fun getFavoriteTvShows(): LiveData<PagedList<ShowEntity>>
    fun getSearchedShowsByGenre(
        category: String,
        page: Int,
        genre: String
    ): LiveData<List<ShowEntity>>

    fun getSearchedShowsByQuery(
        category: String,
        page: Int,
        query: String
    ): LiveData<List<ShowEntity>>

    fun getGenres(category: String): LiveData<List<GenreEntity>>
}