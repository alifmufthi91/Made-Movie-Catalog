package com.example.moviecatalogue.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.data.source.local.room.ShowDao
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val mShowDao: ShowDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(showDao: ShowDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(showDao)
    }

//    fun getAllShows(): DataSource.Factory<Int, ShowEntity> = mMovieCatalogueXDao.getAllShows()
//    }

    fun getMovies(): DataSource.Factory<Int, ShowEntity> =
        mShowDao.getShowsByType(SHOW_MOVIE)


    fun getTvShows(): DataSource.Factory<Int, ShowEntity> =
        mShowDao.getShowsByType(SHOW_TV)

    fun getShow(showType: String, showId: Long): LiveData<ShowEntity> =
        mShowDao.getShow(showType, showId)

    fun getShowEntityById(showId: Long): ShowEntity? =
        mShowDao.getShowEntityById(showId)

//    fun getGenres(category: String): LiveData<List<GenreEntity>> =
//        mMovieCatalogueXDao.getGenresByType(category)

    fun getFavouriteShowsByType(showType: String): DataSource.Factory<Int, ShowEntity> =
        mShowDao.getFavouriteShowsByType(showType)

    fun insertShows(shows: List<ShowEntity>) = mShowDao.insertShows(shows)

    fun insertShow(show: ShowEntity) = mShowDao.insertShow(show)

//    fun insertGenres(genres: List<GenreEntity>) = mMovieCatalogueXDao.insertGenres(genres)

    fun setFavouriteShow(show: ShowEntity, state: Boolean) {
        show.isFavorited = state
        mShowDao.updateShow(show)
    }

    fun updateShow(show: ShowEntity) {
        mShowDao.updateShow(show)
    }

}
