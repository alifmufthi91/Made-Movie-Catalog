package com.example.moviecatalogue_made_s2.utils

import com.example.moviecatalogue_made_s2.model.GenreList
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.model.ShowList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDB {
    @GET("3/discover/{category}")
    fun showList(
        @Path("category") category: String?, @Query("api_key") apiKey: String?, @Query("page") page: Int, @Query(
            "with_genres"
        ) genre: String?
    ): Call<ShowList>

    @GET("3/movie/{movie_id}")
    fun movie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String?): Call<Show>

    @GET("3/tv/{tv_id}")
    fun tv(@Path("tv_id") movieId: Int, @Query("api_key") apiKey: String?): Call<Show>

    @GET("3/search/{category}")
    fun search(
        @Path("category") category: String?, @Query("api_key") apiKey: String?, @Query("page") page: Int, @Query(
            "query"
        ) query: String
    ): Call<ShowList>

    @GET("3/genre/{category}/list")
    fun getGenreList(@Path("category") category: String?, @Query("api_key") apiKey: String?): Call<GenreList>

    @GET("3/discover/{category}")
    fun getTodayReleases(
        @Path("category") category: String?, @Query("api_key") apiKey: String?, @Query(
            "primary_release_date.gte"
        ) gteReleaseDate: String, @Query("primary_release_date.lte") lteReleaseDate: String
    ): Call<ShowList>
}
