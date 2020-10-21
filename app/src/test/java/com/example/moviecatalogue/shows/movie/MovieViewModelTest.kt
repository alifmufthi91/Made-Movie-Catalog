package com.example.moviecatalogue.shows.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalogue.data.LocalMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieViewModel
    @Before
    fun setUp() {
        viewModel = MovieViewModel()
    }

    @Test
    fun getShows() {
        val fakeMovies = LocalMain().getMovies()
        viewModel.movieShows.postValue(fakeMovies)
        assertNotNull(viewModel.getShows().value)
    }
}