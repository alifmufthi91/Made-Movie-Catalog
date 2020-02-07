package com.example.favoritesmovie.ui.activity

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.favoritesmovie.BuildConfig
import com.example.favoritesmovie.R
import com.example.favoritesmovie.db.DatabaseContract
import com.example.favoritesmovie.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_MOVIE_URI
import com.example.favoritesmovie.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_TV_URI
import com.example.favoritesmovie.model.Show
import com.example.favoritesmovie.ui.fragment.FavoriteMovieFragment.Companion.SHOW_MOVIE
import com.example.favoritesmovie.utils.MovieDB
import kotlinx.android.synthetic.main.activity_detail_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailShowActivity : AppCompatActivity() {

    private lateinit var dialogBuilder : AlertDialog

    private var favorited = false
    private var position: Int = 0
    private var showData = Show()
    private lateinit var showType: String

    companion object {
        const val REQUEST_VIEW = 100
        const val RESULT_REMOVED = 201
        const val DETAIL_SHOW = "detailShow"
        const val EXTRA_POSITION = "position"
        const val EXTRA_TYPE = "movie type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_show)
        showData = intent.getParcelableExtra<Show>(DETAIL_SHOW) as Show
        showType = intent.getStringExtra(EXTRA_TYPE) as String

        dialogBuilder = AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title_failed_execute)
            .setMessage(R.string.dialog_text)
            .setNegativeButton("OK") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()

        getShowInfo(showData.movieDbId.toInt())
        Log.d("show type", showType)
        position = intent.getIntExtra(EXTRA_POSITION, 0)

        favorited = true

        displayShowData(showData, favorited)
        ib_back_button.setOnClickListener {
            onBackPressed()
        }
        ib_favorites.setOnClickListener {
            try {
                if (favorited) {
                    when (showType){
                        SHOW_MOVIE -> {
                            val uriWithId = Uri.parse(CONTENT_MOVIE_URI.toString() + "/" + showData.movieDbId)
                            contentResolver.delete(uriWithId, null, null)
                        }
                        else -> {
                            val uriWithId = Uri.parse(CONTENT_TV_URI.toString() + "/" + showData.movieDbId)
                            contentResolver.delete(uriWithId, null, null)
                        }
                    }
                    setFavorite(false)
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.delete_favorite, showData.name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    when (showType){
                        SHOW_MOVIE -> contentResolver.insert(CONTENT_MOVIE_URI, setValues(showData, showType))
                        else -> contentResolver.insert(CONTENT_TV_URI, setValues(showData, showType))
                    }
                    setFavorite(true)
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.add_favorite, showData.name),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showWarningDialog()
            }
        }
    }

    private fun displayShowData(show: Show?, favorited: Boolean) {
        Glide.with(applicationContext).load(show?.getLandscapePhoto()).into(show_cover)
        tv_movie_title.text = show?.name
        tv_movie_overview.text = show?.overview
        displayShowInfo(show)
        setFavorite(favorited)
    }

    private fun showWarningDialog(){
        if (!dialogBuilder.isShowing){
            this.runOnUiThread {
                dialogBuilder.show()
            }
        }
    }

    private fun displayShowInfo(show: Show?) {
        tv_movie_release.text = show?.aired_date
        tv_movie_release.visibility = when (show?.aired_date) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_rating.text = show?.vote_average.toString()
        tv_movie_rating.visibility = when (show?.vote_average) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_popularity.text = show?.popularity.toString()
        tv_movie_popularity.visibility = when (show?.popularity) {
            null -> View.INVISIBLE
            else -> View.VISIBLE
        }
        tv_movie_voter.text = show?.voter.toString()
        tv_movie_voter.visibility = when (show?.voter) {
            0 -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }


    private fun setFavorite(favorited: Boolean) {
        this.favorited = favorited
        if (favorited) {
            ib_favorites.background = resources.getDrawable(R.drawable.ic_favorite, null)
        } else {
            ib_favorites.background = resources.getDrawable(R.drawable.ic_favorite_border, null)
        }
    }

    private fun setValues(show: Show, showType: String) : ContentValues {
        val values = ContentValues()
        values.put(DatabaseContract.FavoritesColumns.NAME,show.name)
        values.put(DatabaseContract.FavoritesColumns.DESCRIPTION,show.overview)
        values.put(DatabaseContract.FavoritesColumns.MOVIEDB_ID, show.movieDbId.toString())
        values.put(DatabaseContract.FavoritesColumns.POSTER, show.imgPath)
        values.put(DatabaseContract.FavoritesColumns.SHOW_TYPE, showType)
        return values
    }

    private fun getShowInfo(showId: Int) {
        Log.d("getShowInfo()", this.toString())
        val builder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val movieDBClient = retrofit.create(MovieDB::class.java)

        val call = when (showType) {
            SHOW_MOVIE -> movieDBClient.movie(showId, BuildConfig.API_KEY)
            else -> movieDBClient.tv(showId, BuildConfig.API_KEY)
        }
        call.enqueue(object : Callback<Show> {
            override fun onResponse(call: Call<Show>, response: Response<Show>) {
                showData = response.body()!!
                GlobalScope.launch(Dispatchers.Main) {
                    displayShowInfo(showData)
                }
                Log.d("getShowInfo : ", "Success.. ${showData.vote_average}")
            }

            override fun onFailure(call: Call<Show>, t: Throwable) {
                Log.d("getShowInfo : ", " Failed..")
            }
        })
    }
}
