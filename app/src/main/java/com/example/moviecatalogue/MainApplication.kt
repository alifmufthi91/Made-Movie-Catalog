package com.example.moviecatalogue

import com.example.moviecatalogue.utils.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


open class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

}