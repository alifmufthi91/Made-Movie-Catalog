package com.example.favoritesmovie.utils

import com.example.favoritesmovie.model.Show
import com.example.favoritesmovie.model.ShowList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDB {
    @GET("3/discover/{category}")
    fun showList(@Path("category") category: String?, @Query("api_key") apiKey: String?): Call<ShowList>

    @GET("3/movie/{movie_id}")
    fun movie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String?): Call<Show>

    @GET("3/tv/{tv_id}")
    fun tv(@Path("tv_id") movieId: Int, @Query("api_key") apiKey: String?): Call<Show>
}
