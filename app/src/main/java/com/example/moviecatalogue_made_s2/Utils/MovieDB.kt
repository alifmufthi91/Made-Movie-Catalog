package com.example.moviecatalogue_made_s2.Utils

import com.example.moviecatalogue_made_s2.Model.ShowList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDB {
    @GET("3/discover/{category}")
    fun showList(@Path("category") category: String?, @Query("api_key") apiKey: String?): Call<ShowList>
}
