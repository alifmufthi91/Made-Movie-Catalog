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

    private lateinit var context: Context
    private val show = MutableLiveData<ShowEntity>()
    private lateinit var type: String
    private var position: Int = 0
    lateinit var showEntity: ShowEntity

    internal fun setShow() {
        this.show.postValue(showEntity)
    }

    internal fun setType(type: String) {
        this.type = type
    }

    var showInfo :LiveData<Resource<ShowEntity>> = Transformations.switchMap(show){ show: ShowEntity ->
        val showId = show.movieDbId.toInt()
        movieCatalogueXRepository.getShowDetail(type, showId)
    }


    fun setDetailData(context: Context, show: ShowEntity, type: String, position: Int) {
        this.context = context
        this.showEntity = show
        this.type = type
        this.position = position
    }

    fun setFavorite(){
        val show = showEntity
        val newState= !show.isFavorited
        movieCatalogueXRepository.setShowFavorited(show, newState)
    }

    fun getShow() = show
    fun getType() = type
}