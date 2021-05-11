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
        remoteDataSource.getTvShows(
            page,
            object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
                override fun onResponse(data: ArrayList<Show>) {
                    val newList = getTvShows().value
                    newList?.addAll(data)
                    tvShowsLiveData.value = newList
                }

                override fun onError(t: Throwable) {
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
            object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
                override fun onResponse(data: ArrayList<Show>) {
                    searchedShowsLiveData.postValue(data)
                }

                override fun onError(t: Throwable) {
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
            object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
                override fun onResponse(data: ArrayList<Show>) {
                    searchedShowsLiveData.postValue(data)
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun loadMoreSearchedShowsByQuery(category: String, page: Int, query: String) {
        remoteDataSource.getSearchedShowByQuery(
            category,
            page,
            query,
            object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
                override fun onResponse(data: ArrayList<Show>) {
                    val newList = getSearchedShows().value
                    newList?.addAll(data)
                    searchedShowsLiveData.value = newList
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun loadMoreSearchedShowsByGenre(category: String, page: Int, genre: String) {
        remoteDataSource.getSearchedShowByGenre(
            category,
            page,
            genre,
            object : RemoteDataSource.CustomCallback<ArrayList<Show>> {
                override fun onResponse(data: ArrayList<Show>) {
                    val newList = getSearchedShows().value
                    newList?.addAll(data)
                    searchedShowsLiveData.value = newList
                }

                override fun onError(t: Throwable) {
                }
            })
    }

    override fun setGenres(category: String) {
        remoteDataSource.getGenres(category, object : RemoteDataSource.CustomCallback<ArrayList<Genre>> {
            override fun onResponse(data: ArrayList<Genre>) {
                genresLiveData.postValue(data)
            }

            override fun onError(t: Throwable) {
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
            object : RemoteDataSource.CustomCallback<Show> {
                override fun onResponse(result: Show) {
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

                override fun onError(t: Throwable) {
                }
            })
        return showResult
    }

    override fun getSearchedShows(): LiveData<ArrayList<Show>> = searchedShowsLiveData

    override fun getGenres(): LiveData<ArrayList<Genre>> = genresLiveData
}