package com.example.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ShowResponse(
    @SerializedName("name", alternate = ["title"])
    var name: String? = null,
    @SerializedName("vote_average")
    var vote_average: Float? = null,
    @SerializedName("release_date", alternate = ["first_air_date"])
    var aired_date: String? = null,
    @SerializedName("poster_path")
    var imgPath: String? = null,
    @SerializedName("overview")
    var overview: String? = null,
    @SerializedName("popularity")
    var popularity: Float? = null,
    @SerializedName("vote_count")
    var voter: Int = 0,
    @SerializedName("id")
    var movieDbId: Long = 0,
    @SerializedName("genres")
    var genreList: ArrayList<GenreResponse>? = null
)