package com.example.moviecatalogue.favourite


import android.os.Bundle
import android.view.MenuItem
import com.example.moviecatalogue.R
import com.example.moviecatalogue.databinding.ActivityFavouriteBinding
import dagger.android.support.DaggerAppCompatActivity

class FavouriteActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val favoritesPagerAdapter =
            FavouritePagerAdapter(
                this,
                supportFragmentManager
            )
        binding.viewPager.adapter = favoritesPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
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
