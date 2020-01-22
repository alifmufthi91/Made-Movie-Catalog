package com.example.moviecatalogue_made_s2.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Parcelize
data class Show (
    @SerializedName("name", alternate=["title"])
    var name:String? = null,
    @SerializedName("vote_average")
    var vote_average:Float? = null,
    @SerializedName("release_date", alternate=["first_air_date"])
    var aired_date:String? = null,
    @SerializedName("poster_path")
    var imgPath:String? = null,
    @SerializedName("overview")
    var overview:String? = null,
    @SerializedName("popularity")
    @Expose
    var popularity:Float? = null,
    @SerializedName("vote_count")
    @Expose
    var voter:Int? = 0
) : Parcelable{

    fun getPortraitPhoto():String = "https://image.tmdb.org/t/p/w188_and_h282_bestv2/$imgPath"
    fun getLandscapePhoto():String = "https://image.tmdb.org/t/p/w500_and_h282_face/$imgPath"
}


