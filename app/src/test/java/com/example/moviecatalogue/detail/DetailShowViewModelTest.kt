package com.example.moviecatalogue.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.ApiResponse
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailShowViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailShowViewModel

    private val dummyMovie = LocalMain().getShow()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var showObserver: Observer<Resource<ShowEntity>>

    private val type = Constant.SHOW_MOVIE
    private val showId: Long = 475557

    @Before
    fun setUp() {
        viewModel = DetailShowViewModel(showRepository)
        viewModel.setShow(showId)
        viewModel.setType(type)
    }

    @Test
    fun getDetailShow() {
        val movie = MutableLiveData<Resource<ShowEntity>>()
        movie.value = Resource.success(dummyMovie)
        `when`(
            showRepository.getShowDetail(
                type,
                showId
            )
        ).thenReturn(movie)

        val showEntity = viewModel.getShow(type, showId).value?.data
        verify(showRepository).getShowDetail(type, showId)
        assertNotNull(showEntity)
        assertEquals(dummyMovie.name, showEntity?.name)
        assertEquals(dummyMovie.overview, showEntity?.overview)

        viewModel.showInfo.observeForever(showObserver)
        verify(showObserver).onChanged(movie.value)
    }
}