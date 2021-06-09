package com.example.moviecatalogue.data.source


import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.local.entity.ShowListEntity
import com.example.moviecatalogue.data.source.remote.response.GenreListResponse
import com.example.moviecatalogue.data.source.remote.response.GenreResponse
import com.example.moviecatalogue.data.source.remote.response.ShowListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.utils.DummyShowData
import com.google.gson.Gson

class LocalMain {
    fun getMovies(): List<ShowEntity> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().movies, ShowListEntity::class.java).list
    }

    fun getTvShows(): List<ShowEntity> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().tvShows, ShowListEntity::class.java).list
    }

    fun getShow() = getMovies()[0]

    fun getMovieGenres(): List<GenreResponse> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().movieGenres, GenreListResponse::class.java).list
    }

    fun getMoviesResponse(): List<ShowResponse> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().movies, ShowListResponse::class.java).list
    }

    fun getTvShowsResponse(): List<ShowResponse> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().tvShows, ShowListResponse::class.java).list
    }
}