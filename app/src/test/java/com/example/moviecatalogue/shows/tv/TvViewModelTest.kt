package com.example.moviecatalogue.shows.tv

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
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
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: TvViewModel

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<ShowEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<ShowEntity>

    @Before
    fun setUp() {
        viewModel = TvViewModel(showRepository)
    }

    @Test
    fun getShows() {
        val dummyTvs = Resource.success(pagedList)
        Mockito.`when`(dummyTvs.data?.size).thenReturn(5)
        val shows = MutableLiveData<Resource<PagedList<ShowEntity>>>()
        shows.value = dummyTvs

        Mockito.`when`(showRepository.getTvShows()).thenReturn(shows)
        val tvsList = viewModel.getShows().value?.data
        verify(showRepository).getTvShows()
        assertNotNull(tvsList)
        assertEquals(5, tvsList?.size)

        viewModel.getShows().observeForever(observer)
        verify(observer).onChanged(dummyTvs)
    }
}