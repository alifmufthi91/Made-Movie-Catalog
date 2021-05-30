package com.example.moviecatalogue.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
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
    constructor(showResponse: ShowResponse, category: String) : this(
        showResponse.movieDbId,
        showResponse.name,
        showResponse.vote_average,
        showResponse.aired_date,
        showResponse.imgPath,
        showResponse.overview,
        showResponse.popularity,
        showResponse.voter,
        showType = category
    ){
        var genres = ""
        showResponse.genreList?.forEachIndexed { index, genreResponse ->
            genres += if (index != 0) {
                ", " + genreResponse.name
            } else {
                genreResponse.name
            }
        }
        this.genreList = genres
    }
    fun updateDataFromEntity(show: ShowEntity){
        this.name = show.name
        this.genreList = show.genreList
        this.aired_date = show.aired_date
        this.overview = show.overview
        this.voter = show.voter
        this.vote_average = show.vote_average
        this.imgPath = show.imgPath
        this.popularity = show.popularity
    }
    fun getPortraitPhoto(): String = "https://image.tmdb.org/t/p/w188_and_h282_bestv2/$imgPath"
    fun getLandscapePhoto(): String = "https://image.tmdb.org/t/p/w500_and_h282_face/$imgPath"
}