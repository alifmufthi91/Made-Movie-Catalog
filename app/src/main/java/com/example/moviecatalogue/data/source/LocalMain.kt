package com.example.moviecatalogue.data.source


import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.GenreList
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.model.ShowList
import com.example.moviecatalogue.utils.DummyShowData
import com.google.gson.Gson

class LocalMain {
    fun getMovies(): ArrayList<Show> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().movies, ShowList::class.java).list
    }

    fun getTvShows(): ArrayList<Show> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().tvShows, ShowList::class.java).list
    }

    fun getShow() = getMovies()[0]

    fun getMovieGenres(): ArrayList<Genre> {
        val gson = Gson()
        return gson.fromJson(DummyShowData().movieGenres, GenreList::class.java).list
    }
}