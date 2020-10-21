package com.example.moviecatalogue.data


import com.example.moviecatalogue.model.Show
import com.example.moviecatalogue.model.ShowList
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
}