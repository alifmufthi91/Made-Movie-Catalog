package com.example.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.local.room.MovieCatalogueXDao
import java.util.*
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val mMovieCatalogueXDao: MovieCatalogueXDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        const val SHOW_MOVIE = "Movie"
        const val SHOW_TV = "Tv"

        fun getInstance(movieCatalogueXDao: MovieCatalogueXDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(movieCatalogueXDao)
    }

//    fun getAllShows(): DataSource.Factory<Int, ShowEntity> = mMovieCatalogueXDao.getAllShows()
//    }

    fun getMovies(): DataSource.Factory<Int, ShowEntity> =
        mMovieCatalogueXDao.getShowsByType(SHOW_MOVIE.toLowerCase(Locale.getDefault()))


    fun getTvShows(): DataSource.Factory<Int, ShowEntity> =
        mMovieCatalogueXDao.getShowsByType(SHOW_TV.toLowerCase(Locale.getDefault()))

    fun getShow(showType: String, showId: Long): LiveData<ShowEntity> =
        mMovieCatalogueXDao.getShow(showType, showId)

    fun getGenres(category: String): LiveData<List<GenreEntity>> =
        mMovieCatalogueXDao.getGenresByType(category)

    fun getFavouriteShowsByType(showType: String): DataSource.Factory<Int, ShowEntity> =
        mMovieCatalogueXDao.getFavouriteShowsByType(showType)

    fun insertShows(shows: List<ShowEntity>) = mMovieCatalogueXDao.insertShows(shows)

    fun insertShow(show: ShowEntity) = mMovieCatalogueXDao.insertShow(show)

    fun insertGenres(genres: List<GenreEntity>) = mMovieCatalogueXDao.insertGenres(genres)

    fun setFavouriteShow(show: ShowEntity, state: Boolean) {
        show.isFavorited = state
        mMovieCatalogueXDao.updateShow(show)
    }

    fun updateShow(show: ShowEntity){
        mMovieCatalogueXDao.updateShow(show)
    }

}
