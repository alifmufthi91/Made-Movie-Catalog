package com.example.moviecatalogue.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.source.local.LocalDataSource
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.remote.ApiResponse
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.data.source.remote.response.GenreResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.utils.AppExecutors
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.utils.LiveDataTestUtil
import com.example.moviecatalogue.utils.PagedListUtil
import com.example.moviecatalogue.vo.Resource
import com.nhaarman.mockitokotlin2.any
import junit.framework.TestCase.assertNotNull
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
    private val movieList = dummyData.getMovies()
    private val tvShowList = dummyData.getTvShows()
    private val show = dummyData.getShow()
    private val genreList = dummyData.getMovieGenres()
    private val moviesResponse = dummyData.getMoviesResponse()
    private val tvShowResponse = dummyData.getTvShowsResponse()

    private val page = 1
    private val movieCategory = Constant.SHOW_MOVIE
    private val tvCategory = Constant.SHOW_TV
    private val showId: Long = 475557
    private val genre = "Action"
    private val query = "A"

    @Test
    fun getMovies() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ShowEntity>
        `when`(local.getMovies()).thenReturn(dataSourceFactory)
        repository.getMovies(page)
        val moviesEntities = Resource.success(PagedListUtil.mockPagedList(movieList))
        verify(local).getMovies()
        assertNotNull(moviesEntities.data)
    }

    @Test
    fun getTvShows() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ShowEntity>
        `when`(local.getTvShows()).thenReturn(dataSourceFactory)
        repository.getTvShows(page)
        val tvEntities = Resource.success(PagedListUtil.mockPagedList(tvShowList))
        verify(local).getTvShows()
        assertNotNull(tvEntities.data)
    }

    @Test
    fun getShowDetail() {
        val dummyShow = MutableLiveData<ShowEntity>()
        dummyShow.value = show
        `when`(local.getShow(movieCategory, showId)).thenReturn(dummyShow)
        val showsEntity = LiveDataTestUtil.getValue(repository.getShowDetail(movieCategory, showId))
        verify(local).getShow(movieCategory, showId)
        assertNotNull(showsEntity)
    }

    @Test
    fun getFavoritesMovie() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ShowEntity>
        `when`(local.getFavouriteShowsByType(movieCategory)).thenReturn(dataSourceFactory)
        repository.getFavoriteMovies()
        val favoritesMovie = PagedListUtil.mockPagedList(movieList)
        verify(local).getFavouriteShowsByType(movieCategory)
        assertNotNull(favoritesMovie)
    }

    @Test
    fun getFavoritesTv() {
        val dataSourceFactory = mock(DataSource.Factory::class.java) as DataSource.Factory<Int, ShowEntity>
        `when`(local.getFavouriteShowsByType(tvCategory)).thenReturn(dataSourceFactory)
        repository.getFavoriteTvShows()
        val favoritesTv = PagedListUtil.mockPagedList(tvShowList)
        verify(local).getFavouriteShowsByType(tvCategory)
        assertNotNull(favoritesTv)
    }

    @Test
    fun getGenres() {
        val dummyGenres = genreList
        val genreResponse = ApiResponse.success(dummyGenres)
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.CustomCallback<ApiResponse<List<GenreResponse>>>)
                .onResponse(genreResponse)
            null
        }.`when`(remote).getGenres(eq(movieCategory), any())
        val genreEntities = LiveDataTestUtil.getValue(repository.getGenres(movieCategory))
        verify(remote).getGenres(eq(movieCategory), any())
        assertNotNull(genreEntities)
    }

    @Test
    fun getSearchedShowsByGenre() {
        val dummyMovies = moviesResponse
        val searchedResponse = ApiResponse.success(dummyMovies)
        doAnswer { invocation ->
            (invocation.arguments[3] as RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>>)
                .onResponse(searchedResponse)
            null
        }.`when`(remote).getSearchedShowByGenre(eq(movieCategory), eq(page), eq(genre), any())
        val showEntities = LiveDataTestUtil.getValue(repository.getSearchedShowsByGenre(movieCategory, page, genre))
        verify(remote).getSearchedShowByGenre(eq(movieCategory), eq(page), eq(genre), any())
        assertNotNull(showEntities)
    }

    @Test
    fun getSearchedShowsByQuery() {
        val dummyTv = tvShowResponse
        val searchedResponse = ApiResponse.success(dummyTv)
        doAnswer { invocation ->
            (invocation.arguments[3] as RemoteDataSource.CustomCallback<ApiResponse<List<ShowResponse>>>)
                .onResponse(searchedResponse)
            null
        }.`when`(remote).getSearchedShowByQuery(eq(tvCategory), eq(page), eq(query), any())
        val showEntities = LiveDataTestUtil.getValue(repository.getSearchedShowsByQuery(tvCategory, page, query))
        verify(remote).getSearchedShowByQuery(eq(tvCategory), eq(page), eq(query), any())
        assertNotNull(showEntities)
    }

}