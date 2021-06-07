package com.example.moviecatalogue.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ShowListResponse(
    @SerializedName("results")
    var list: List<ShowResponse>,
    @SerializedName("total_results")
    var total: Int,
    @SerializedName("total_pages")
    var pages: Int,
    @SerializedName("page")
    var page: Int
)