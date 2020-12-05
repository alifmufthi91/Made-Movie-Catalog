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
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.MainApplication
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.shows.movie.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue.utils.Utility
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import com.example.moviecatalogue.vo.Status
import kotlinx.android.synthetic.main.activity_detail_show.*
import javax.inject.Inject


@Suppress("NAME_SHADOWING")
class DetailShowActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DetailShowViewModel

    companion object {
        const val REQUEST_VIEW = 100
        const val DETAIL_SHOW = "detailShow"
        const val EXTRA_POSITION = "position"
        const val EXTRA_TYPE = "movie type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_show)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[DetailShowViewModel::class.java]
        viewModel.apply {
            setDetailData(
                this@DetailShowActivity,
                intent.getParcelableExtra(DETAIL_SHOW) as ShowEntity,
                intent.getStringExtra(EXTRA_TYPE) as String,
                intent.getIntExtra(EXTRA_POSITION, 0)
            )
            setShow()
            displayShowData()
            displayShowInfo(viewModel.showEntity)
            showInfo.observe(this@DetailShowActivity, Observer {
                if (it != null) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            displayShowInfo(it.data)
                            showEntity.voter = it.data?.voter ?: showEntity.voter
                            showEntity.popularity = it.data?.popularity
                            showEntity.vote_average = it.data?.vote_average
                            displayShowData()
                        }
                        else -> {
                        }
                    }
                }
            })
        }
        displayShowData()
        iv_share.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder.from(this).apply {
                setType(mimeType)
                setChooserTitle("Bagikan aplikasi ini sekarang.")
                setText(resources.getString(R.string.share_text, viewModel.getShow().value?.name))
                startChooser()
            }
        }
        iv_favorites.setOnClickListener {
            viewModel.showEntity.isFavorited = !viewModel.showEntity.isFavorited
            viewModel.setShow()
            viewModel.setFavorite()
            if(!viewModel.showEntity.isFavorited){
                updateFavoriteIcon(false)
                Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.delete_favorite,
                            viewModel.showEntity.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
            }else{
                updateFavoriteIcon(true)
                Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.add_favorite,
                            viewModel.showEntity.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun displayShowData() {
        val showData: ShowEntity? = viewModel.getShow().value
        Glide.with(this).load(showData?.getLandscapePhoto()).apply(
            RequestOptions.placeholderOf(R.drawable.ic_image_black)
                .error(R.drawable.ic_image_error_black)
        ).into(show_cover)
        tv_show_title.text = showData?.name
        tv_show_overview.text = showData?.overview
        if (showData != null) {
            if(showData.isFavorited){
                updateFavoriteIcon(true)
            }
        }
    }

    private fun displayShowInfo(show: ShowEntity?) {
        tv_show_release.text = show?.aired_date
        tv_show_release.visibility = when (show?.aired_date) {
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
        tv_show_genre.text = show?.genreList
        tv_show_genre.visibility = when (show?.genreList) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
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

}


