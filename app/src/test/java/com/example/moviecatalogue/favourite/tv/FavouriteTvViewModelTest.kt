package com.example.moviecatalogue.favourite.tv

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavouriteTvViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavoriteTvViewModel

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<PagedList<ShowEntity>>

    @Mock
    private lateinit var pagedList: PagedList<ShowEntity>

    @Before
    fun setUp() {
        viewModel = FavoriteTvViewModel(showRepository)
    }

    @Test
    fun getFavoriteTv() {
        val dummyTvs = pagedList
        `when`(dummyTvs.size).thenReturn(5)
        val tvShows= MutableLiveData<PagedList<ShowEntity>>()
        tvShows.value = dummyTvs

        `when`(showRepository.getFavoriteTvShows()).thenReturn(tvShows)
        val tvList = viewModel.getFavoriteTvShows().value
        verify(showRepository).getFavoriteTvShows()
        assertNotNull(tvList)
        assertEquals(5, tvList?.size)

        viewModel.getFavoriteTvShows().observeForever(observer)
        verify(observer).onChanged(dummyTvs)
    }
}