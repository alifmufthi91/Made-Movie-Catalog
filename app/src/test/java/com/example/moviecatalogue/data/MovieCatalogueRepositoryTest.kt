package com.example.moviecatalogue.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.utils.AppExecutors
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.utils.LiveDataTestUtil
import com.example.moviecatalogue.utils.PagedListUtil
import com.example.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.any
import junit.framework.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class MovieCatalogueRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val repository = FakeShowRepository(remote, local, appExecutors)
    private val dummyData = LocalMain()
    private val moviesResponses = dummyData.getMovies()
    private val tvShowsResponses = dummyData.getTvShows()
    private val showDetailResponse = dummyData.getShow()

    private val page = 1
    private val type = Constant.SHOW_MOVIE
    private val showId: Long = 475557

    @Test
    fun getMovies() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ShowEntity>
        `when`(local.getMovies()).thenReturn(dataSourceFactory)
        repository.getMovies(page)
        val moviesEntities = Resource.success(PagedListUtil.mockPagedList(moviesResponses))
        verify(local).getMovies()
        assertNotNull(moviesEntities.data)
    }

    @Test
    fun getTvShows() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ShowEntity>
        `when`(local.getTvShows()).thenReturn(dataSourceFactory)
        repository.getTvShows(page)
        val tvEntities = Resource.success(PagedListUtil.mockPagedList(tvShowsResponses))
        verify(local).getTvShows()
        assertNotNull(tvEntities.data)
    }

    @Test
    fun getShowDetail() {
        val dummyShow = MutableLiveData<ShowEntity>()
        dummyShow.value = showDetailResponse
        `when`(local.getShow(type, showId)).thenReturn(dummyShow)
        val showsEntity = LiveDataTestUtil.getValue(repository.getShowDetail(type, showId))
        verify(local).getShow(type, showId)
        assertNotNull(showsEntity)
    }

}