package com.example.moviecatalogue_made_s2.model

import com.google.gson.annotations.SerializedName

data class GenreList(
    @SerializedName("genres")
    var list: ArrayList<Genre>
)