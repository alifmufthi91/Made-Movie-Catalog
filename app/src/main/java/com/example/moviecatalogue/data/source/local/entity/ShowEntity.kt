package com.example.moviecatalogue.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "showentities")
data class ShowEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieDbId")
    var movieDbId: Long,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "vote_average")
    var vote_average: Float? = null,
    @ColumnInfo(name = "aired_date")
    var aired_date: String? = null,
    @ColumnInfo(name = "img_path")
    var imgPath: String? = null,
    @ColumnInfo(name = "overview")
    var overview: String? = null,
    @ColumnInfo(name = "popularity")
    var popularity: Float? = null,
    @ColumnInfo(name = "voter")
    var voter: Int = 0,
    @ColumnInfo(name = "genreList")
    var genreList: String? = null,
    @ColumnInfo(name = "showType")
    var showType: String? = null,
    @ColumnInfo(name = "isFavorited")
    var isFavorited: Boolean = false
): Parcelable{

    fun getPortraitPhoto(): String = "https://image.tmdb.org/t/p/w188_and_h282_bestv2/$imgPath"
    fun getLandscapePhoto(): String = "https://image.tmdb.org/t/p/w500_and_h282_face/$imgPath"
}