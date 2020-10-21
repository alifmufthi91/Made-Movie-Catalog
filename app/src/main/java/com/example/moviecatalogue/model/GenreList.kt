package com.example.moviecatalogue.model

import com.google.gson.annotations.SerializedName

data class GenreList(
    @SerializedName("genres")
    var list: ArrayList<Genre>
)