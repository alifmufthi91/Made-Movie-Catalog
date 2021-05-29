package com.example.moviecatalogue.data.source.local.entity

import com.google.gson.annotations.SerializedName

data class ShowListEntity(
    var list: List<ShowEntity>,
    var total: Int,
    var pages: Int,
    var page: Int
)