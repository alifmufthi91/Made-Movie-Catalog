package com.example.moviecatalogue_made_s2.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Show (
    @SerializedName("name", alternate=["title"])
    @Expose
    var name:String?,
    @SerializedName("vote_average")
    @Expose
    var vote_average:Float?,
    @SerializedName("release_date", alternate=["first_air_date"])
    @Expose
    var aired_date:String?,
    @SerializedName("poster_path")
    @Expose
    var imgPath:String?,
    @SerializedName("overview")
    @Expose
    var overview:String?,
    @SerializedName("popularity")
    @Expose
    var popularity:Float?,
    @SerializedName("vote_count")
    @Expose
    var voter:Int?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    fun getPortraitPhoto():String = "https://image.tmdb.org/t/p/w188_and_h282_bestv2/$imgPath"
    fun getLandscapePhoto():String = "https://image.tmdb.org/t/p/w500_and_h282_face/$imgPath"
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeValue(vote_average)
        parcel.writeString(aired_date)
        parcel.writeString(imgPath)
        parcel.writeString(overview)
        parcel.writeValue(popularity)
        parcel.writeValue(voter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Show> {
        override fun createFromParcel(parcel: Parcel): Show {
            return Show(parcel)
        }

        override fun newArray(size: Int): Array<Show?> {
            return arrayOfNulls(size)
        }
    }

}
