package com.example.moviecatalogue.data.source.local.entity

data class ShowListEntity(
    var list: List<ShowEntity>,
    var total: Int,
    var pages: Int,
    var page: Int
)