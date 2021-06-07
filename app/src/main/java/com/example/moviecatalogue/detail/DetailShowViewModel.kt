package com.example.moviecatalogue.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.data.ShowRepository
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.vo.Resource
import javax.inject.Inject

class DetailShowViewModel @Inject constructor(
    val showRepository: ShowRepository

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

    var showInfo: LiveData<Resource<ShowEntity>> = Transformations.switchMap(showId) { showId ->
        getShow(type, showId)
    }

    fun setDetailData(showId: Long, type: String, position: Int) {
        this.type = type
        this.position = position
        setShow(showId)
    }

    fun setFavorite(status: Boolean) {
        showEntity.isFavorited = status
        showRepository.updateShow(showEntity)
    }

    fun getShow(type: String, showId: Long): LiveData<Resource<ShowEntity>> {
        return showRepository.getShowDetail(type, showId)
    }
}