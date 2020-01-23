package com.example.moviecatalogue_made_s2.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Show(
    @SerializedName("name", alternate = ["title"])
    @Expose
    var name: String? = null,
    @SerializedName("vote_average")
    @Expose
    var vote_average: Float? = null,
    @SerializedName("release_date", alternate = ["first_air_date"])
    @Expose
    var aired_date: String? = null,
    @SerializedName("poster_path")
    @Expose
    var imgPath: String? = null,
    @SerializedName("overview")
    @Expose
    var overview: String? = null,
    @SerializedName("popularity")
    @Expose
    var popularity: Float? = null,
    @SerializedName("vote_count")
    @Expose
    var voter: Int? = 0,
    @SerializedName("id")
    @Expose
    var movieDbId: Long = 0
) : Parcelable {
    fun getPortraitPhoto(): String = "https://image.tmdb.org/t/p/w188_and_h282_bestv2/$imgPath"
    fun getLandscapePhoto(): String = "https://image.tmdb.org/t/p/w500_and_h282_face/$imgPath"
}


