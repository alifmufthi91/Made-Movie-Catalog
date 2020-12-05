package com.example.moviecatalogue.shows.tv


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue.R
import com.example.moviecatalogue.listener.CustomRecyclerViewScrollListener
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import com.example.moviecatalogue.vo.Status
import kotlinx.android.synthetic.main.fragment_tv_list.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class TvFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var listViewModel: TvViewModel
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
        return inflater.inflate(R.layout.fragment_tv_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_TV)
        listViewModel.setShows()
        listViewModel.getShows().observe(viewLifecycleOwner, Observer { shows ->
            if (shows != null) {
                when (shows.status) {
                    Status.SUCCESS -> {
                        listShowAdapter.submitList(shows.data)
                        listShowAdapter.notifyDataSetChanged()
                    }
                    else -> {}
                }
            }
        })
    }


    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_TV)
        listShowAdapter.notifyDataSetChanged()
        mLayoutManger = LinearLayoutManager(context)
        rv_tv.layoutManager = mLayoutManger
        scrollListener = CustomRecyclerViewScrollListener(mLayoutManger as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            CustomRecyclerViewScrollListener.OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        rv_tv.addOnScrollListener(scrollListener)
        rv_tv.adapter = listShowAdapter
        listViewModel = ViewModelProviders.of(this, viewModelFactory)[TvViewModel::class.java]
    }

    private fun loadMoreData() {
        Handler().postDelayed({
            listViewModel.loadMore()
            scrollListener.setLoaded()
            rv_tv.post {
                listShowAdapter.notifyDataSetChanged()
            }
        }, 100)
    }


}
