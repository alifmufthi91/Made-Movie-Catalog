package com.example.moviecatalogue.shows.tv

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.source.MovieCatalogueXRepository
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
class TvViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: TvViewModel

    @Mock
    private lateinit var movieCatalogueXRepository: MovieCatalogueXRepository

    @Mock
    private lateinit var observer: Observer<ArrayList<Show>>

    @Before
    fun setUp() {
        viewModel = TvViewModel(movieCatalogueXRepository)
    }

    @Test
    fun getShows() {
        val dummyTvShows = LocalMain().getTvShows()
        val tvShows = MutableLiveData<ArrayList<Show>>()
        tvShows.value = dummyTvShows

        Mockito.`when`(movieCatalogueXRepository.getTvShows()).thenReturn(tvShows)
        val tvShowList = viewModel.getShows().value
        verify(movieCatalogueXRepository).getTvShows()
        assertNotNull(tvShowList)
        assertEquals(20, tvShowList?.size)

        viewModel.getShows().observeForever(observer)
        verify(observer).onChanged(dummyTvShows)
    }
}