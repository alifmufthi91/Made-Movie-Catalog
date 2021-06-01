package com.example.moviecatalogue.favourite.tv


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_favourite_tv_list.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvFragment : DaggerFragment() {

    @Inject
    lateinit var listViewModel: FavoriteTvViewModel
    private lateinit var listShowAdapter: ListShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_tv_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_TV)
        listViewModel.getFavoriteTvShows().observe(viewLifecycleOwner, Observer{
            Log.d("favorite tvs: ", it.toString())
            if (it != null) {
                listShowAdapter.submitList(it)
            }
        })
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_TV)
        listShowAdapter.notifyDataSetChanged()
        rv_favourite_tv.layoutManager = LinearLayoutManager(activity)
        rv_favourite_tv.adapter = listShowAdapter

    }


}
