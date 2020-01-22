package com.example.moviecatalogue_made_s2.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.adapter.ListShowAdapter
import com.example.moviecatalogue_made_s2.ui.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 */
class TvFragment : Fragment() {

    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var listViewModel: ListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", "tv")
        listViewModel.setShows("tv")
        listViewModel.getShows("tv").observe(this, Observer { Shows ->
            if(Shows != null) {
                listShowAdapter.setData(Shows)
            }
        })

    }


    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter()
        listShowAdapter.notifyDataSetChanged()
        rv_shows.layoutManager = LinearLayoutManager(activity)
        rv_shows.adapter = listShowAdapter
        listViewModel = ViewModelProvider(activity!!.viewModelStore, ViewModelProvider.NewInstanceFactory()).get(
            ListViewModel::class.java)
    }


}
