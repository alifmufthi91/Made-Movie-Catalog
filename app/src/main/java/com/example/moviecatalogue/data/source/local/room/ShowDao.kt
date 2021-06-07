package com.example.moviecatalogue.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.moviecatalogue.data.source.local.entity.ShowEntity

@Dao
interface ShowDao {

    @Query("SELECT * FROM showentities")
    fun getAllShows(): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM showentities WHERE showType = :showType")
    fun getShowsByType(showType: String): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM showentities WHERE isFavorited = 1 AND showType = :showType")
    fun getFavouriteShowsByType(showType: String): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM showentities WHERE movieDbId = :showId AND showType = :showType")
    fun getShow(showType: String, showId: Long): LiveData<ShowEntity>

    @Query("SELECT * FROM showentities WHERE movieDbId = :showId")
    fun getShowEntityById(showId: Long): ShowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShows(shows: List<ShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShow(show: ShowEntity)

//    @Query("SELECT * FROM genreentities WHERE category = :category")
//    fun getGenresByType(category: String): LiveData<List<GenreEntity>>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGenres(genres: List<GenreEntity>)

    @Update
    fun updateShow(show: ShowEntity)
}