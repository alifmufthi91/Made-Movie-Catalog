package com.example.moviecatalogue.data.source.remote

import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.data.source.remote.response.GenreListResponse
import com.example.moviecatalogue.data.source.remote.response.GenreResponse
import com.example.moviecatalogue.data.source.remote.response.ShowListResponse
import com.example.moviecatalogue.data.source.remote.response.ShowResponse
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import com.example.moviecatalogue.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {

    companion object {

        @Volatile
        private var instance: RemoteDataSource? = null

    }

    fun getMovies(page: Int, callback: CustomCallback<ApiResponse<List<ShowResponse>>>) {
        EspressoIdlingResource.increment()
        apiService.showList(
            SHOW_MOVIE,
            BuildConfig.API_KEY,
            page,
            null
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getTvShows(page: Int, callback: CustomCallback<ApiResponse<List<ShowResponse>>>) {
        EspressoIdlingResource.increment()
        apiService.showList(
            SHOW_TV,
            BuildConfig.API_KEY,
            page,
            null
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getSearchedShowByQuery(
        category: String,
        page: Int,
        query: String,
        callback: CustomCallback<ApiResponse<List<ShowResponse>>>
    ) {
        EspressoIdlingResource.increment()
        apiService.search(
            category,
            BuildConfig.API_KEY,
            page,
            query
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getSearchedShowByGenre(
        category: String,
        page: Int,
        genre: String,
        callback: CustomCallback<ApiResponse<List<ShowResponse>>>
    ) {
        EspressoIdlingResource.increment()
        apiService.showList(
            category,
            BuildConfig.API_KEY,
            page,
            genre
        ).enqueue(object : Callback<ShowListResponse> {
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getShowDetail(
        type: String,
        showId: Long,
        callback: CustomCallback<ApiResponse<ShowResponse>>
    ) {
        EspressoIdlingResource.increment()
        val call = when (type) {
            SHOW_MOVIE -> showId.let { apiService.movie(it, BuildConfig.API_KEY) }
            SHOW_TV -> showId.let { apiService.tv(it, BuildConfig.API_KEY) }
            else -> {
                return
            }
        }
        call.enqueue(object : Callback<ShowResponse> {
            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    fun getGenres(category: String, callback: CustomCallback<ApiResponse<List<GenreResponse>>>) {
        EspressoIdlingResource.increment()
        apiService.getGenreList(
            category,
            BuildConfig.API_KEY
        ).enqueue(object : Callback<GenreListResponse> {
            override fun onResponse(
                call: Call<GenreListResponse>,
                response: Response<GenreListResponse>
            ) {
                response.body()?.let {
                    callback.onResponse(ApiResponse.success(it.list))
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<GenreListResponse>, t: Throwable) {
                callback.onError(t)
                EspressoIdlingResource.decrement()
            }
        })
    }

    interface CustomCallback<T> {
        fun onResponse(data: T)
        fun onError(t: Throwable)
    }
}