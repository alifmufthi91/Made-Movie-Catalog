package com.example.moviecatalogue_made_s2.utils

import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.model.ShowList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDB {
    @GET("3/discover/{category}")
    fun showList(@Path("category") category: String?, @Query("api_key") apiKey: String?, @Query("page") page: Int): Call<ShowList>

    @GET("3/movie/{movie_id}")
    fun movie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String?): Call<Show>

    @GET("3/tv/{tv_id}")
    fun tv(@Path("tv_id") movieId: Int, @Query("api_key") apiKey: String?): Call<Show>
}
