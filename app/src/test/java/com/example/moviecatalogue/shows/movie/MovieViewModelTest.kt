package com.example.moviecatalogue.shows.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var movieCatalogueXRepository: MovieCatalogueXRepository

    @Mock
    private lateinit var observer: Observer<ArrayList<Show>>

    @Before
    fun setUp() {
        viewModel = MovieViewModel(movieCatalogueXRepository)
    }

    @Test
    fun getShows() {
        val dummyMovies = LocalMain().getMovies()
        val movies = MutableLiveData<ArrayList<Show>>()
        movies.value = dummyMovies

        `when`(movieCatalogueXRepository.getMovies()).thenReturn(movies)
        val movieList = viewModel.getShows().value
        verify(movieCatalogueXRepository).getMovies()
        assertNotNull(movieList)
        assertEquals(20, movieList?.size)

        viewModel.getShows().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}