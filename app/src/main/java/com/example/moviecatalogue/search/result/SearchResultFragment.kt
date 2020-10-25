package com.example.moviecatalogue.search.result


import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.listener.CustomRecyclerViewScrollListener
import com.example.moviecatalogue.search.SearchShowAdapter
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.utils.Constant
import kotlinx.android.synthetic.main.fragment_search_result.*

/**
 * A simple [Fragment] subclass.
 */
class SearchResultFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchShowAdapter: SearchShowAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var scrollListener: CustomRecyclerViewScrollListener


    companion object {
        const val GRID_COLUMN = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_result_fragment.text = getString(R.string.search_result)
        showRecyclerList()
        searchViewModel.getShows().observe(viewLifecycleOwner, Observer { shows ->
            if (shows != null) {
                searchShowAdapter.setData(shows)
                if (shows.size < 1) {
                    empty_result_fragment_search.visibility = View.VISIBLE
                } else {
                    empty_result_fragment_search.visibility = View.GONE
                }
            }
        })
    }


    private fun showRecyclerList() {
        searchViewModel = ViewModelProvider(
            requireActivity().viewModelStore,
            ViewModelProvider.NewInstanceFactory()
        ).get(
            SearchViewModel::class.java
        )
        searchShowAdapter = SearchShowAdapter(activity as Activity, searchViewModel.getCategory())
        searchShowAdapter.notifyDataSetChanged()
        mLayoutManager = GridLayoutManager(activity, GRID_COLUMN)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (searchShowAdapter.getItemViewType(position)) {
                    Constant.VIEW_TYPE_ITEM -> return 1
                    Constant.VIEW_TYPE_LOADING -> return GRID_COLUMN
                }
                return -1
            }

        }
        rv_search.layoutManager = mLayoutManager
        scrollListener =
            CustomRecyclerViewScrollListener(
                mLayoutManager
            )
        scrollListener.setOnLoadMoreListener(object :
            CustomRecyclerViewScrollListener.OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        rv_search.addOnScrollListener(scrollListener)
        rv_search.adapter = searchShowAdapter
    }

    private fun loadMoreData() {
        searchShowAdapter.addLoadingView()
        Handler().postDelayed({
            searchViewModel.loadMore()
            searchShowAdapter.removeLoadingView()
            scrollListener.setLoaded()
            if (this.isVisible) {
                rv_search.post {
                    searchShowAdapter.notifyDataSetChanged()
                }
            }
        }, 100)
    }


}
