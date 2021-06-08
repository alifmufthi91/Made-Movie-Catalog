package com.example.moviecatalogue.favourite.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviecatalogue.data.source.LocalMain
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FavouriteMovieViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: FavoriteMovieViewModel

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<PagedList<ShowEntity>>

    @Mock
    private lateinit var pagedList: PagedList<ShowEntity>

    @Before
    fun setUp() {
        viewModel = FavoriteMovieViewModel(showRepository)
    }

    @Test
    fun getFavoriteMovies() {
        val dummyMovies = pagedList
        `when`(dummyMovies.size).thenReturn(5)
        val movies = MutableLiveData<PagedList<ShowEntity>>()
        movies.value = dummyMovies

        `when`(showRepository.getFavoriteMovies()).thenReturn(movies)
        val movieList = viewModel.getFavoriteMovies().value
        verify(showRepository).getFavoriteMovies()
        assertNotNull(movieList)
        assertEquals(5, movieList?.size)

        viewModel.getFavoriteMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}