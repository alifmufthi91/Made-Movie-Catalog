package com.example.moviecatalogue.shows.tv

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalogue.data.LocalMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: TvViewModel
    @Before
    fun setUp() {
        viewModel = TvViewModel()
    }

    @Test
    fun getShows() {
        val fakeTvShows = LocalMain().getTvShows()
        viewModel.tvShows.postValue(fakeTvShows)
        assertNotNull(viewModel.getShows().value)
    }
}