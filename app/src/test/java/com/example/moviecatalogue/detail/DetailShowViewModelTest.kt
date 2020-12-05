package com.example.moviecatalogue.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailShowViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailShowViewModel
    private val dummyMovie = LocalMain().getMovies()[0]

    @Mock
    private lateinit var movieCatalogueXRepository: MovieCatalogueXRepository

    @Mock
    private lateinit var showObserver: Observer<Show>

    @Before
    fun setUp() {
        viewModel = DetailShowViewModel(movieCatalogueXRepository)
    }

    @Test
    fun getDetailShow() {
        val movie = MutableLiveData<Show>()
        movie.value = dummyMovie

        Mockito.`when`(
            movieCatalogueXRepository.getShowDetail(
                "movie",
                dummyMovie.movieDbId.toInt()
            )
        ).thenReturn(movie)
        viewModel.setShow(dummyMovie)
        viewModel.setType("movie")

        val showEntity = viewModel.getShowInfo().value
        verify(movieCatalogueXRepository).getShowDetail("movie", dummyMovie.movieDbId.toInt())
        assertNotNull(showEntity)
        assertEquals(dummyMovie.name, showEntity?.name)
        assertEquals(dummyMovie.overview, showEntity?.overview)

        viewModel.getShowInfo().observeForever(showObserver)
        verify(showObserver).onChanged(dummyMovie)
    }
}