package com.example.moviecatalogue.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.remote.RemoteDataSource

class FakeMovieCatalogueXRepository (private val remoteDataSource: RemoteDataSource) :
    FakeDataSource {


    override fun getMovies(page: Int): LiveData<ArrayList<Show>> {
        val moviesLiveData = MutableLiveData<ArrayList<Show>>()
        remoteDataSource.getMovies(page, object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
            override fun onResponse(data: ArrayList<Show>) {
                moviesLiveData.postValue(data)
            }

            override fun onError(t: Throwable) {
            }
        })

        return moviesLiveData
    }


    override fun getTvShows(page: Int): LiveData<ArrayList<Show>> {
        val tvShowsLiveData = MutableLiveData<ArrayList<Show>>()
        remoteDataSource.getTvShows(page, object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
            override fun onResponse(data: ArrayList<Show>) {
                tvShowsLiveData.postValue(data)
            }

            override fun onError(t: Throwable) {
            }
        })

        return tvShowsLiveData
    }

    override fun getShowDetail(type: String, showId: Int): LiveData<Show> {
        val showsDetailLiveData = MutableLiveData<Show>()
        remoteDataSource.getShowDetail(type, showId, object : RemoteDataSource.CustomCallback<Show> {
            override fun onResponse(data: Show) {
                showsDetailLiveData.postValue(data)
            }

            override fun onError(t: Throwable) {
            }
        })
        return showsDetailLiveData
    }

    override fun getGenres(category: String): LiveData<ArrayList<Genre>> {
        val genresLiveData = MutableLiveData<ArrayList<Genre>>()
        remoteDataSource.getGenres(category, object : RemoteDataSource.CustomCallback<ArrayList<Genre>>{
            override fun onResponse(data: ArrayList<Genre>) {
                genresLiveData.postValue(data)
            }

            override fun onError(t: Throwable) {
            }
        })
        return genresLiveData
    }

}