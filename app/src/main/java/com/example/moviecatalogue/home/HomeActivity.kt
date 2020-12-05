package com.example.moviecatalogue.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.moviecatalogue.MainApplication
import com.example.moviecatalogue.R
import com.example.moviecatalogue.favourite.FavouriteActivity
import com.example.moviecatalogue.search.SearchActivity
import com.example.moviecatalogue.settings.SettingsActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val mIntent = Intent(this@HomeActivity, SettingsActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_favorites -> {
                val mIntent = Intent(this@HomeActivity, FavouriteActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_search -> {
                val mIntent = Intent(this@HomeActivity, SearchActivity::class.java)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
