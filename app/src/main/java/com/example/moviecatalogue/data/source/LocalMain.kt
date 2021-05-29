package com.example.moviecatalogue.data.source


import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.GenreListEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.local.entity.ShowListEntity
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

    fun getMovieGenres(): List<GenreEntity> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().movieGenres, GenreListEntity::class.java).list
    }
}