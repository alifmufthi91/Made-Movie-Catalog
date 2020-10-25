package com.example.moviecatalogue.data.model

import com.google.gson.annotations.SerializedName

data class GenreList(
    @SerializedName("genres")
    var list: ArrayList<Genre>
)