package com.example.moviecatalogue.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.utils.GlideApp
import com.example.moviecatalogue.utils.Utility
import com.example.moviecatalogue.vo.Status
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_show.*
import javax.inject.Inject


@Suppress("NAME_SHADOWING")
class DetailShowActivity : DaggerAppCompatActivity() {

    @Inject
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
        viewModel.apply {
            setDetailData(
//                this@DetailShowActivity,
                intent.getLongExtra(DETAIL_SHOW, 0),
                intent.getStringExtra(EXTRA_TYPE) as String,
                intent.getIntExtra(EXTRA_POSITION, 0)
            )
            showInfo.observe(this@DetailShowActivity, Observer { response ->
                Log.d("response detail: ", response.toString())
                if (response != null) {
                    when (response.status) {
                        Status.SUCCESS -> {
                            response.data?.let { show ->
                                showEntity = show
//                                updateShow(show)
                                displayShowInfo(showEntity)
                            }
                        }
                        else -> {
                        }
                    }
                }
            })
        }
        iv_share.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder.from(this).apply {
                setType(mimeType)
                setChooserTitle("Bagikan aplikasi ini sekarang.")
                setText(resources.getString(R.string.share_text, viewModel.getShow().name))
                startChooser()
            }
        }
        iv_favorites.setOnClickListener {
            viewModel.apply {
                setFavorite(!showEntity.isFavorited)
                if(!showEntity.isFavorited){
                    updateFavoriteIcon(false)
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.delete_favorite,
                            showEntity.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    updateFavoriteIcon(true)
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.add_favorite,
                            showEntity.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun displayShowInfo(show: ShowEntity) {
        GlideApp.with(this).load(show.getLandscapePhoto()).apply(
            RequestOptions.placeholderOf(R.drawable.ic_image_black)
                .error(R.drawable.ic_image_error_black)
        ).into(show_cover)
        tv_show_title.text = show.name
        tv_show_overview.text = show.overview
        tv_show_release.text = show.aired_date
        tv_show_release.visibility = View.VISIBLE
        tv_movie_rating.text = show.vote_average.toString()
        tv_movie_rating.visibility = View.VISIBLE
        tv_movie_popularity.text = show.popularity?.toLong()?.let { Utility.longToSuffixes(it) }
        tv_movie_popularity.visibility = View.VISIBLE
        tv_movie_voter.text = Utility.longToSuffixes(show.voter.toLong())
        tv_movie_voter.visibility = View.VISIBLE
        tv_show_genre.text = show.genreList
        tv_show_genre.visibility = View.VISIBLE
        updateFavoriteIcon(show.isFavorited)
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


