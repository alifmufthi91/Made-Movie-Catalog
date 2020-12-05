package com.example.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    @SerializedName("genres")
    var list: List<GenreResponse>
)