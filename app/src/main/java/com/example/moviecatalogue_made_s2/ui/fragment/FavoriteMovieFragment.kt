package com.example.moviecatalogue_made_s2.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.adapter.ListShowAdapter
import com.example.moviecatalogue_made_s2.db.FavoritesHelper
import com.example.moviecatalogue_made_s2.model.Show
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity
import com.example.moviecatalogue_made_s2.ui.activity.DetailShowActivity.Companion.EXTRA_POSITION
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue_made_s2.ui.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var listViewModel: ListViewModel
    private lateinit var listShows: ArrayList<Show>
    private lateinit var favoritesHelper: FavoritesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_MOVIE)
        favoritesHelper = FavoritesHelper.getInstance(activity!!.applicationContext)
        favoritesHelper.open()
        listShows = favoritesHelper.getDataByShowType(SHOW_MOVIE)
        listShowAdapter.setData(listShows)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesHelper.close()
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
        listShowAdapter.notifyDataSetChanged()
        rv_shows.layoutManager = LinearLayoutManager(activity)
        rv_shows.adapter = listShowAdapter
        listViewModel = ViewModelProvider(
            activity!!.viewModelStore,
            ViewModelProvider.NewInstanceFactory()
        ).get(
            ListViewModel::class.java
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == DetailShowActivity.RESULT_REMOVED) {
                val position = data.getIntExtra(EXTRA_POSITION, 0)
                listShowAdapter.removeItem(position)
            }
        }
    }

}
