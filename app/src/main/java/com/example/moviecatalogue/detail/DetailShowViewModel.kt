package com.example.moviecatalogue.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.MovieCatalogueXRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class DetailShowViewModel @Inject constructor(
    val movieCatalogueXRepository: MovieCatalogueXRepository

) : ViewModel() {

    val showId = MutableLiveData<Long>()
    private lateinit var type: String
    private var position: Int = 0
    lateinit var showEntity: ShowEntity

    internal fun setShow(showId: Long) {
        this.showId.value = showId
    }

    internal fun setType(type: String) {
        this.type = type
    }

    var showInfo :LiveData<Resource<ShowEntity>> = Transformations.switchMap(showId){ showId ->
        movieCatalogueXRepository.getShowDetail(type, showId)
    }

    fun setDetailData(showId: Long, type: String, position: Int) {
//        this.context = context
        this.type = type
        this.position = position
        setShow(showId)
    }

    fun updateShow(show: ShowEntity){
        showEntity.genreList = show.genreList
        showEntity.voter = show.voter
        showEntity.popularity = show.popularity
        showEntity.overview = show.overview
        showEntity.vote_average = show.vote_average
        showEntity.imgPath = show.imgPath
        showEntity.name = show.name
    }

    fun setFavorite(status: Boolean) {
        showEntity.isFavorited = status
        movieCatalogueXRepository.updateShow(showEntity)
    }


    fun getShow() = showEntity
    fun getType() = type
}