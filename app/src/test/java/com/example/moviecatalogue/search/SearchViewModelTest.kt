package com.example.moviecatalogue.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
class SearchViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<List<ShowEntity>>

    @Mock
    private lateinit var pagedList: List<ShowEntity>

    @Before
    fun setUp() {
        viewModel = SearchViewModel(showRepository)
    }

    @Test
    fun getShows() {
        val dummyMovies = pagedList
        `when`(dummyMovies.size).thenReturn(5)
        val movies = MutableLiveData<List<ShowEntity>>()
        movies.value = dummyMovies

        `when`(showRepository.getSearchedShows()).thenReturn(movies)
        val searchedList = viewModel.getShows().value
        verify(showRepository).getSearchedShows()
        assertNotNull(searchedList)
        assertEquals(5, searchedList?.size)

        viewModel.getShows().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}