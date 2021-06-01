package com.example.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.local.room.MovieCatalogueXDao
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val mMovieCatalogueXDao: MovieCatalogueXDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(movieCatalogueXDao: MovieCatalogueXDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(movieCatalogueXDao)
    }

//    fun getAllShows(): DataSource.Factory<Int, ShowEntity> = mMovieCatalogueXDao.getAllShows()
//    }

    fun getMovies(): DataSource.Factory<Int, ShowEntity> =
        mMovieCatalogueXDao.getShowsByType(SHOW_MOVIE)


    fun getTvShows(): DataSource.Factory<Int, ShowEntity> =
        mMovieCatalogueXDao.getShowsByType(SHOW_TV)

    fun getShow(showType: String, showId: Long): LiveData<ShowEntity> =
        mMovieCatalogueXDao.getShow(showType, showId)

    fun getShowEntityById(showId: Long): ShowEntity? =
        mMovieCatalogueXDao.getShowEntityById(showId)

//    fun getGenres(category: String): LiveData<List<GenreEntity>> =
//        mMovieCatalogueXDao.getGenresByType(category)

    fun getFavouriteShowsByType(showType: String): DataSource.Factory<Int, ShowEntity> =
        mMovieCatalogueXDao.getFavouriteShowsByType(showType)

    fun insertShows(shows: List<ShowEntity>) = mMovieCatalogueXDao.insertShows(shows)

    fun insertShow(show: ShowEntity) = mMovieCatalogueXDao.insertShow(show)

//    fun insertGenres(genres: List<GenreEntity>) = mMovieCatalogueXDao.insertGenres(genres)

    fun setFavouriteShow(show: ShowEntity, state: Boolean) {
        show.isFavorited = state
        mMovieCatalogueXDao.updateShow(show)
    }

    fun updateShow(show: ShowEntity) {
        mMovieCatalogueXDao.updateShow(show)
    }

}
