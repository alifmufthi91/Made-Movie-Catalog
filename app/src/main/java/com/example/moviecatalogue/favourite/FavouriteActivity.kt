package com.example.moviecatalogue.favourite


import android.os.Bundle
import android.view.MenuItem
import com.example.moviecatalogue.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        val favoritesPagerAdapter =
            FavouritePagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager.adapter = favoritesPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = getString(R.string.favorites)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
