package com.example.moviecatalogue.data.source.remote

import com.example.moviecatalogue.data.source.remote.response.GenreListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("3/discover/{category}")
    fun showList(
        @Path("category") category: String?,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int,
        @Query(
            "with_genres"
        ) genre: String?
    ): Call<ShowListResponse>

    @GET("3/movie/{movie_id}")
    fun movie(@Path("movie_id") movieId: Long, @Query("api_key") apiKey: String?): Call<ShowResponse>

    @GET("3/tv/{tv_id}")
    fun tv(@Path("tv_id") tvId: Long, @Query("api_key") apiKey: String?): Call<ShowResponse>

    @GET("3/search/{category}")
    fun search(
        @Path("category") category: String?,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int,
        @Query(
            "query"
        ) query: String
    ): Call<ShowListResponse>

    @GET("3/genre/{category}/list")
    fun getGenreList(
        @Path("category") category: String?,
        @Query("api_key") apiKey: String?
    ): Call<GenreListResponse>

    @GET("3/discover/{category}")
    fun getTodayReleases(
        @Path("category") category: String?, @Query("api_key") apiKey: String?, @Query(
            "primary_release_date.gte"
        ) gteReleaseDate: String, @Query("primary_release_date.lte") lteReleaseDate: String
    ): Call<ShowListResponse>
}
