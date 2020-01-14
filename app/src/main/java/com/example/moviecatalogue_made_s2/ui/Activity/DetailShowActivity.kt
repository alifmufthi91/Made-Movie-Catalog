package com.example.moviecatalogue_made_s2.ui.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.moviecatalogue_made_s2.Model.Show
import com.example.moviecatalogue_made_s2.R
import kotlinx.android.synthetic.main.activity_detail_show.*

class DetailShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_show)
        val show = intent.getParcelableExtra<Show>("detailShow")
        displayShowData(show)
    }

    private fun displayShowData(show: Show?){
        Glide.with(applicationContext).load(show?.getLandscapePhoto()).into(show_cover)
        tv_movie_title.text = show?.name
        tv_movie_overview.text = show?.overview
        tv_movie_release.text = show?.aired_date
        tv_movie_rating.text = show?.vote_average.toString()
        tv_movie_popularity.text = show?.popularity.toString()
        tv_movie_voter.text = show?.voter.toString()
    }
}
