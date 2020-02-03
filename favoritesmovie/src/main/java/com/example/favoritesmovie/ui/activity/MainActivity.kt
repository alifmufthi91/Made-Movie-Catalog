package com.example.favoritesmovie.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.favoritesmovie.R
import com.example.favoritesmovie.adapter.FavoritesPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var builder : AlertDialog

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

        builder = AlertDialog.Builder(this@MainActivity)
            .setTitle(R.string.dialog_title_failed_retrieve)
            .setMessage(R.string.dialog_text)
            .setNegativeButton("OK") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()

    }

    fun showWarningDialog(){
        if (!builder.isShowing){
            runOnUiThread {
                builder.show()
            }
        }
    }

    override fun onDestroy() {
        if (builder.isShowing){
            builder.dismiss()
        }
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
