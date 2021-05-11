package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.Show

interface FakeDataSource {
    fun getMovies(page: Int): LiveData<ArrayList<Show>>
    fun getTvShows(page: Int): LiveData<ArrayList<Show>>
    fun getShowDetail(type: String, showId: Int): LiveData<Show>
    fun getGenres(category: String): LiveData<ArrayList<Genre>>
}