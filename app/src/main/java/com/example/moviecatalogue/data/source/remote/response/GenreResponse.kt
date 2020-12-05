package com.example.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
)