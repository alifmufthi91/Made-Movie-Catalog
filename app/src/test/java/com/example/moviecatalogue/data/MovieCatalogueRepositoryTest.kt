package com.example.moviecatalogue.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.source.LocalMain
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.utils.LiveDataTestUtil
import com.nhaarman.mockitokotlin2.any
import junit.framework.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class MovieCatalogueRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val repository = FakeMovieCatalogueXRepository(remote)
    private val moviesResponses = LocalMain().getMovies()
    private val tvShowsResponses = LocalMain().getTvShows()
    private val showDetailResponse = LocalMain().getShow()
    private val movieGenresResponses = LocalMain().getMovieGenres()

    private val page = 1
    private val type = "Movie"
    private val showId = 475557

    @Test
    fun getMovies() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.CustomCallback<ArrayList<Show>>)
                .onResponse(moviesResponses)
            null
        }.`when`(remote).getMovies(eq(page), any())
        val moviesEntities = LiveDataTestUtil.getValue(repository.getMovies(page))
        verify(remote).getMovies(eq(page), any())
        assertNotNull(moviesEntities)
    }

    @Test
    fun getTvShows() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.CustomCallback<ArrayList<Show>>)
                .onResponse(tvShowsResponses)
            null
        }.`when`(remote).getTvShows(eq(page), any())
        val showsEntities = LiveDataTestUtil.getValue(repository.getTvShows(page))
        verify(remote).getTvShows(eq(page), any())
        assertNotNull(showsEntities)
    }

    @Test
    fun getShowDetail() {
        doAnswer { invocation ->
            (invocation.arguments[2] as RemoteDataSource.CustomCallback<Show>)
                .onResponse(showDetailResponse)
            null
        }.`when`(remote).getShowDetail(eq(type), eq(showId), any())
        val showsEntity = LiveDataTestUtil.getValue(repository.getShowDetail(type, showId))
        verify(remote).getShowDetail(eq(type), eq(showId), any())
        assertNotNull(showsEntity)
    }

    @Test
    fun getGenres() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.CustomCallback<ArrayList<Genre>>)
                .onResponse(movieGenresResponses)
            null
        }.`when`(remote).getGenres(eq(type), any())
        val genreEntities = LiveDataTestUtil.getValue(repository.getGenres(type))
        verify(remote).getGenres(eq(type), any())
        assertNotNull(genreEntities)
    }
}