package com.example.moviecatalogue.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.ShowEntity
import com.example.moviecatalogue.databinding.ActivityDetailShowBinding
import com.example.moviecatalogue.utils.Utility
import com.example.moviecatalogue.vo.Status
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


@Suppress("NAME_SHADOWING")
class DetailShowActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: DetailShowViewModel
    private lateinit var binding: ActivityDetailShowBinding

    companion object {
        const val DETAIL_SHOW = "detailShow"
        const val EXTRA_POSITION = "position"
        const val EXTRA_TYPE = "movie type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailShowBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.d(EXTRA_TYPE, intent.getStringExtra(EXTRA_TYPE).toString())
        viewModel.apply {
            setDetailData(
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
                                displayShowInfo(showEntity)
                            }
                        }
                        else -> {
                        }
                    }
                }
            })
        }
        binding.ivShare.setOnClickListener {
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder(this).apply {
                setType(mimeType)
                setChooserTitle("Bagikan aplikasi ini sekarang.")
                setText(resources.getString(R.string.share_text, viewModel.getShow().name))
                startChooser()
            }
        }
        binding.ivFavorites.setOnClickListener {
            viewModel.apply {
                setFavorite(!showEntity.isFavorited)
                if (!showEntity.isFavorited) {
                    updateFavoriteIcon(false)
                    Toast.makeText(
                        applicationContext,
                        getString(
                            R.string.delete_favorite,
                            showEntity.name
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
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
        Glide.with(this).load(show.getLandscapePhoto()).apply(
            RequestOptions.placeholderOf(R.drawable.ic_image_black)
                .error(R.drawable.ic_image_not_exist)
        ).into(binding.showCover)
        binding.tvShowTitle.text = show.name
        binding.tvShowOverview.text = show.overview
        binding.tvShowRelease.text = show.aired_date
        binding.tvShowRelease.visibility = View.VISIBLE
        binding.tvMovieRating.text = show.vote_average.toString()
        binding.tvMovieRating.visibility = View.VISIBLE
        binding.tvMoviePopularity.text =
            show.popularity?.toLong()?.let { Utility.longToSuffixes(it) }
        binding.tvMoviePopularity.visibility = View.VISIBLE
        binding.tvMovieVoter.text = Utility.longToSuffixes(show.voter.toLong())
        binding.tvMovieVoter.visibility = View.VISIBLE
        binding.tvShowGenre.text = show.genreList
        binding.tvShowGenre.visibility = View.VISIBLE
        updateFavoriteIcon(show.isFavorited)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorites.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite
                )
            )
        } else {
            binding.ivFavorites.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_border
                )
            )
        }
    }

}


