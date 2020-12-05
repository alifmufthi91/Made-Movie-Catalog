package com.example.moviecatalogue.utils

import com.example.moviecatalogue.MainApplication
import com.example.moviecatalogue.di.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
//        ViewModelModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        ActivitiesModule::class,
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<MainApplication> {

//    fun inject(activity: HomeActivity)

    @Component.Factory
    abstract class Builder: AndroidInjector.Factory<MainApplication>
//    @Component.Builder
//    interface Builder {
//        @BindsInstance
//        fun applicationBind(application: Application): Builder
//        fun build(): AppComponent
//    }

}