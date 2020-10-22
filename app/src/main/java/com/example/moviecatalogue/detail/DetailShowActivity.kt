package com.example.moviecatalogue.detail

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviecatalogue.R
import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_MOVIE_URI
import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_TV_URI
import com.example.moviecatalogue.model.Genre
import com.example.moviecatalogue.model.Show
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue.utils.Utility
import kotlinx.android.synthetic.main.activity_detail_show.*


@Suppress("NAME_SHADOWING")
class DetailShowActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailShowViewModel

    companion object {
        const val REQUEST_VIEW = 100
        const val DETAIL_SHOW = "detailShow"
        const val EXTRA_POSITION = "position"
        const val EXTRA_TYPE = "movie type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_show)
        viewModel = ViewModelProvider(
            this,
            DetailShowViewModelFactory(
                this,
                intent.getParcelableExtra(DETAIL_SHOW) as Show,
                intent.getStringExtra(EXTRA_TYPE) as String,
                intent.getIntExtra(EXTRA_POSITION, 0)
            )
        )[DetailShowViewModel::class.java]
        viewModel.apply {
            showLiveData.observe(this@DetailShowActivity, Observer {
                displayShowInfo(it)
            })
            isFavourite.observe(this@DetailShowActivity, Observer {
                updateFavoriteIcon(it)
            })
            getShowInfo()
            getFavouriteStatus()
        }
        displayShowData()
        iv_share.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder.from(this).apply {
                setType(mimeType)
                setChooserTitle("Bagikan aplikasi ini sekarang.")
                setText(resources.getString(R.string.share_text,viewModel.show?.name))
                startChooser()
            }
        }
        iv_favorites.setOnClickListener {
            try {
                if (viewModel.isFavourite.value as Boolean) {
                    when (viewModel.type) {
                        SHOW_MOVIE -> {
                            val uriWithId =
                                Uri.parse(CONTENT_MOVIE_URI.toString() + "/" + viewModel.showLiveData.value?.movieDbId)
                            contentResolver.delete(uriWithId, null, null)
                        }
                        else -> {
                            val uriWithId =
                                Uri.parse(CONTENT_TV_URI.toString() + "/" + viewModel.showLiveData.value?.movieDbId)
                            contentResolver.delete(uriWithId, null, null)
                        }
                    }
                    setFavorite(false)
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.delete_favorite,
                            viewModel.showLiveData.value?.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    when (viewModel.type) {
                        SHOW_MOVIE -> contentResolver.insert(
                            CONTENT_MOVIE_URI,
                            viewModel.showLiveData.value?.let { it ->
                                viewModel.setValues(
                                    it,
                                    viewModel.type as String
                                )
                            }
                        )
                        else -> contentResolver.insert(
                            CONTENT_TV_URI,
                            viewModel.showLiveData.value?.let { it ->
                                viewModel.setValues(
                                    it,
                                    viewModel.type as String
                                )
                            }
                        )
                    }
                    setFavorite(true)
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.add_favorite,
                            viewModel.showLiveData.value?.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun displayShowData() {
        val showData: Show? = viewModel.showLiveData.value
        Glide.with(this).load(showData?.getLandscapePhoto()).into(show_cover)
        tv_movie_title.text = showData?.name
        tv_movie_overview.text = showData?.overview
    }

    private fun displayShowInfo(show: Show?) {
        tv_movie_release.text = show?.aired_date
        tv_movie_release.visibility = when (show?.aired_date) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_rating.text = show?.vote_average.toString()
        tv_movie_rating.visibility = when (show?.vote_average) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_popularity.text = show?.popularity?.toLong()?.let { Utility.longToSuffixes(it) }
        tv_movie_popularity.visibility = when (show?.popularity) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_voter.text = show?.voter?.toLong()?.let { Utility.longToSuffixes(it) }
        tv_movie_voter.visibility = when (show?.voter) {
            0 -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_genre.text = getGenres(show?.genreList)
        tv_movie_genre.visibility = when (show?.genreList) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }


    private fun setFavorite(favoriteStatus: Boolean) {
        viewModel.setFavorite(favoriteStatus)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean){
        if (isFavorite) {
            iv_favorites.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite
                )
            )
        } else {
            iv_favorites.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_border
                )
            )
        }
    }

    private fun getGenres(list: ArrayList<Genre>?): String? {
        var genres = ""
        list?.forEach {
            genres += if (it != list[0]) {
                ", " + it.name
            } else {
                it.name
            }
        }
        return genres
    }
}
