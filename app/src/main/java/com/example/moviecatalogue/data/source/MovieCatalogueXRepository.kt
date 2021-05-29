package com.example.moviecatalogue.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.data.model.GenreList
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.data.model.ShowList
import com.example.moviecatalogue.data.source.remote.RemoteDataSource
import com.example.moviecatalogue.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Response

class MovieCatalogueXRepository private constructor(private val remoteDataSource: RemoteDataSource) :
    MovieCatalogueXDataSource {

    private val moviesLiveData = MutableLiveData<ArrayList<Show>>()
    private val tvShowsLiveData = MutableLiveData<ArrayList<Show>>()
    private val searchedShowsLiveData = MutableLiveData<ArrayList<Show>>()
    private val genresLiveData = MutableLiveData<ArrayList<Genre>>()

    companion object {
        @Volatile
        private var instance: MovieCatalogueXRepository? = null
        fun getInstance(remoteData: RemoteDataSource): MovieCatalogueXRepository =
            instance ?: synchronized(this) {
                instance ?: MovieCatalogueXRepository(remoteData)
            }
    }

    override fun setMovies(page: Int) {
        remoteDataSource.getMovies(page, object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
            override fun onResponse(data: ArrayList<Show>) {
                moviesLiveData.postValue(data)
            }

            override fun onError(t: Throwable) {
            }
        })
    }

    override fun loadMoreMovies(page: Int) {
        remoteDataSource.getMovies(page, object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
            override fun onResponse(data: ArrayList<Show>) {
                val newList = getMovies().value
                newList?.addAll(data)
                moviesLiveData.value = newList
            }

            override fun onError(t: Throwable) {
            }
        })
    }

    override fun setTvShows(page: Int) {
        remoteDataSource.getTvShows(
            page,
            object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
                override fun onResponse(data: ArrayList<Show>) {
                    tvShowsLiveData.postValue(data)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun loadMoreTvShows(page: Int) {
        remoteDataSource.getTvShows(page, object : RemoteDataSource.LoadTvShowsCallback {
            override fun onResponse(
                call: Call<ShowList>,
                response: Response<ShowList>
            ) {
                val result = response.body()
                if (result != null) {
                    val newList = getTvShows().value
                    newList?.addAll(result.list)
                    tvShowsLiveData.value = newList
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
    }

    override fun setSearchedShowsByQuery(
        category: String,
        page: Int,
        query: String
    ) {
        remoteDataSource.getSearchedShowByQuery(
            category,
            page,
            query,
            object : RemoteDataSource.LoadSearchedShowCallback {
                override fun onResponse(
                    call: Call<ShowList>,
                    response: Response<ShowList>
                ) {
                    val result = response.body()
                    if (result != null) {
                        searchedShowsLiveData.postValue(result.list)
                    }
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(call: Call<ShowList>, t: Throwable) {
                    EspressoIdlingResource.decrement()
                }
            })
    }

    override fun setSearchedShowsByGenre(
        category: String,
        page: Int,
        genre: String
    ) {
        remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre,
            object : RemoteDataSource.LoadSearchedShowCallback {
                override fun onResponse(
                    call: Call<ShowList>,
                    response: Response<ShowList>
                ) {
                    val result = response.body()
                    if (result != null) {
                        searchedShowsLiveData.postValue(result.list)
                    }
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(call: Call<ShowList>, t: Throwable) {
                    EspressoIdlingResource.decrement()
                }
            })
    }

    override fun loadMoreSearchedShowsByQuery(category: String, page: Int, query: String) {
        remoteDataSource.getSearchedShowByQuery(
            category,
            page,
            query,
            object : RemoteDataSource.LoadSearchedShowCallback {
                override fun onResponse(
                    call: Call<ShowList>,
                    response: Response<ShowList>
                ) {
                    val result = response.body()
                    if (result != null) {
                        val newList = getSearchedShows().value
                        newList?.addAll(result.list)
                        searchedShowsLiveData.value = newList
                    }
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(call: Call<ShowList>, t: Throwable) {
                    EspressoIdlingResource.decrement()
                }
            })
    }

    override fun loadMoreSearchedShowsByGenre(category: String, page: Int, genre: String) {
        remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre,
            object : RemoteDataSource.LoadSearchedShowCallback {
                override fun onResponse(
                    call: Call<ShowList>,
                    response: Response<ShowList>
                ) {
                    val result = response.body()
                    if (result != null) {
                        val newList = getSearchedShows().value
                        newList?.addAll(result.list)
                        searchedShowsLiveData.value = newList
                    }
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(call: Call<ShowList>, t: Throwable) {
                    EspressoIdlingResource.decrement()
                }
            })
    }

    override fun setGenres(category: String) {
        remoteDataSource.getGenres(category, object : RemoteDataSource.LoadGenresCallback {
            override fun onResponse(
                call: Call<GenreList>,
                response: Response<GenreList>
            ) {
                val results = response.body()
                if (results != null) {
                    genresLiveData.postValue(results.list)
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<GenreList>, t: Throwable) {
                EspressoIdlingResource.decrement()
            }
        })
    }

    override fun getMovies(): LiveData<ArrayList<Show>> = moviesLiveData

    override fun getTvShows(): LiveData<ArrayList<Show>> = tvShowsLiveData

    override fun getShowDetail(type: String, showId: Int): LiveData<Show> {
        val showResult = MutableLiveData<Show>()
        remoteDataSource.getShowDetail(
            type,
            showId,
            object : RemoteDataSource.LoadShowDetailCallback {
                override fun onResponse(
                    call: Call<Show>,
                    response: Response<Show>
                ) {
                    val result = response.body()
                    if (result != null) {
                        val showData = Show(
                            result.name,
                            result.vote_average,
                            result.aired_date,
                            result.imgPath,
                            result.overview,
                            result.popularity,
                            result.voter,
                            result.movieDbId,
                            result.genreList,
                            result.showType
                        )
                        showResult.postValue(showData)
                    }
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(call: Call<Show>, t: Throwable) {
                    EspressoIdlingResource.decrement()
                }
            })
        return showResult
    }

    override fun getSearchedShows(): LiveData<ArrayList<Show>> = searchedShowsLiveData

    override fun getGenres(): LiveData<ArrayList<Genre>> = genresLiveData
}