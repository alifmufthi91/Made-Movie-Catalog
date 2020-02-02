package com.example.favoritesmovie.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favoritesmovie.R
import com.example.favoritesmovie.adapter.FavoritesPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val favoritesPagerAdapter =
            FavoritesPagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager.adapter = favoritesPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

    }


}
