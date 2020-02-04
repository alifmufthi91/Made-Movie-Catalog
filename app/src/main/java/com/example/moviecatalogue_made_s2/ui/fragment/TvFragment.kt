package com.example.moviecatalogue_made_s2.ui.fragment


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.adapter.ListShowAdapter
import com.example.moviecatalogue_made_s2.ui.listener.CustomRecyclerViewScrollListener
import com.example.moviecatalogue_made_s2.ui.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


/**
 * A simple [Fragment] subclass.
 */
class TvFragment : Fragment() {

    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var listViewModel: ListViewModel
    private lateinit var mLayoutManger: RecyclerView.LayoutManager
    private lateinit var scrollListener: CustomRecyclerViewScrollListener
    private var currentPage = 1

    companion object {
        const val SHOW_TV = "Tv"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_TV)
        listViewModel.setShows(SHOW_TV, currentPage)
        listViewModel.getShows(SHOW_TV).observe(this, Observer { Shows ->
            if (Shows != null) {
                listShowAdapter.setData(Shows)
            }
        })

    }


    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_TV)
        listShowAdapter.notifyDataSetChanged()
        mLayoutManger = LinearLayoutManager(activity)
        rv_shows.layoutManager = mLayoutManger
        scrollListener = CustomRecyclerViewScrollListener(mLayoutManger as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : CustomRecyclerViewScrollListener.OnLoadMoreListener{
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        rv_shows.addOnScrollListener(scrollListener)
        rv_shows.adapter = listShowAdapter
        listViewModel = ViewModelProvider(
            activity!!.viewModelStore,
            ViewModelProvider.NewInstanceFactory()
        ).get(
            ListViewModel::class.java
        )
    }

    private fun loadMoreData() {
        listShowAdapter.addLoadingView()
        //disini get data//
        Handler().postDelayed({
            listViewModel.setShows(SHOW_TV, ++currentPage)
            //end//
            listShowAdapter.removeLoadingView()
            scrollListener.setLoaded()
            rv_shows.post {
                listShowAdapter.notifyDataSetChanged()
            }
        }, 2000)
    }


}
