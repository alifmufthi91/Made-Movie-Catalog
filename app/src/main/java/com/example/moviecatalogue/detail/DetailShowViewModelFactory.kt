package com.example.moviecatalogue.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.model.Show

class DetailShowViewModelFactory(
    private val context: Context,
    private val show: Show,
    private val type: String,
    private val position: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailShowViewModel(context, show, type, position) as T
    }

}