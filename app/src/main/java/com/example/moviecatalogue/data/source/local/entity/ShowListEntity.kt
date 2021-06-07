package com.example.moviecatalogue.data.source.local.entity

import com.google.gson.annotations.SerializedName

data class ShowListEntity(
    @SerializedName("results")
    var list: List<ShowEntity>,
    @SerializedName("total_results")
    var total: Int,
    @SerializedName("total_pages")
    var pages: Int,
    @SerializedName("page")
    var page: Int
)