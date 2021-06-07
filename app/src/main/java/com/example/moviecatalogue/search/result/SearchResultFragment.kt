package com.example.moviecatalogue.search.result


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.databinding.FragmentSearchResultBinding
import com.example.moviecatalogue.search.SearchShowAdapter
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchResultFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)
    }
    private lateinit var searchShowAdapter: SearchShowAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
//    private lateinit var scrollListener: CustomRecyclerViewScrollListener


    companion object {
        const val GRID_COLUMN = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchResultFragment.text = getString(R.string.search_result)
        showRecyclerList()
        searchViewModel.apply {
            searchedShows.observe(viewLifecycleOwner, Observer { shows ->
                Log.d("shows :", shows.toString())
                if (shows != null) {
                    searchShowAdapter.setData(shows)
                }
            })
        }
    }


    private fun showRecyclerList() {
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
        binding.rvSearch.layoutManager = mLayoutManager
        binding.rvSearch.adapter = searchShowAdapter

//        scrollListener =
//            CustomRecyclerViewScrollListener(
//                mLayoutManager
//            )
//        scrollListener.setOnLoadMoreListener(object :
//            CustomRecyclerViewScrollListener.OnLoadMoreListener {
//            override fun onLoadMore() {
//                loadMoreData()
//            }
//        })
//        rv_search.addOnScrollListener(scrollListener)
    }

//    private fun loadMoreData() {
//        searchShowAdapter.addLoadingView()
//        Handler().postDelayed({
//            searchViewModel.loadMore()
//            searchShowAdapter.removeLoadingView()
//            scrollListener.setLoaded()
//            if (this.isVisible) {
//                rv_search.post {
//                    searchShowAdapter.notifyDataSetChanged()
//                }
//            }
//        }, 100)
//    }


}
