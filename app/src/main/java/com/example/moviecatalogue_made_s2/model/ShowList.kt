package com.example.moviecatalogue_made_s2.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShowList(
    @SerializedName("results")
    @Expose
    var list: ArrayList<Show>,
    @SerializedName("total_results")
    @Expose
    var total: Int,
    @SerializedName("total_pages")
    @Expose
    var pages: Int,
    @SerializedName("page")
    @Expose
    var page: Int
)