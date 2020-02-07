package com.example.moviecatalogue_made_s2.ui.activity


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.adapter.FavoritesPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class FavoritesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val favoritesPagerAdapter =
            FavoritesPagerAdapter(
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
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
