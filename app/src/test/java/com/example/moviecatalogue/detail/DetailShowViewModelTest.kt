package com.example.moviecatalogue.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalogue.data.LocalMain
import kotlinx.coroutines.withContext
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailShowViewModelTest{

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailShowViewModel
    @Before
    fun setUp() {
        viewModel = DetailShowViewModel(null,null,null,null)
    }

    @Test
    fun getDetailShow() {
        val fakeMovie = LocalMain().getMovies()[0]
        viewModel.showLiveData.postValue(fakeMovie)
        assertNotNull(viewModel.showLiveData.value)
        assertEquals(viewModel.showLiveData.value?.name,"Joker")
    }
}