package com.example.moviecatalogue

import com.example.moviecatalogue.utils.AppComponent
import com.example.moviecatalogue.utils.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


open class MainApplication : DaggerApplication(){

//    companion object{
//        lateinit var appComponent: AppComponent
//    }

    override fun onCreate() {
        super.onCreate()
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

}