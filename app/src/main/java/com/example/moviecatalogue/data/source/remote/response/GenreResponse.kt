package com.example.moviecatalogue.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GenreResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
)