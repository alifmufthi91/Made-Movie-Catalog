package com.example.moviecatalogue.search

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.databinding.ActivitySearchByGenreBinding
import com.example.moviecatalogue.search.result.SearchResultFragment
import com.example.moviecatalogue.utils.Constant
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SearchByGenreActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: SearchByGenreViewModel
    private lateinit var searchShowAdapter: SearchShowAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var binding: ActivitySearchByGenreBinding
//    private lateinit var scrollListener: CustomRecyclerViewScrollListener


    companion object {
        const val SELECTED_GENRE = "selectedGenre"
        const val SELECTED_CATEGORY = "selectedCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchByGenreBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.searchResult.text = getString(R.string.search_result)

        val category = intent.getStringExtra(SELECTED_CATEGORY)
        Log.d("category: ",category.toString())
        val genre = intent.getParcelableExtra<GenreEntity>(SELECTED_GENRE)

        supportActionBar?.title = genre?.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.apply {
            setCategory(category as String)
            setGenre(genre?.id.toString())
            setShows(category)
        }
        showRecyclerList()
        viewModel.getShows().observe(this, Observer {
            if (it != null) {
                searchShowAdapter.setData(it)
            }
        })
    }

    private fun showRecyclerList() {
        searchShowAdapter = SearchShowAdapter(this, viewModel.getCategory())
        searchShowAdapter.notifyDataSetChanged()
        mLayoutManager = GridLayoutManager(this, SearchResultFragment.GRID_COLUMN)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (searchShowAdapter.getItemViewType(position)) {
                    Constant.VIEW_TYPE_ITEM -> return 1
                    Constant.VIEW_TYPE_LOADING -> return SearchResultFragment.GRID_COLUMN
                }
                return -1
            }

        }
        binding.rvSearchGenre.layoutManager = mLayoutManager
        binding.rvSearchGenre.adapter = searchShowAdapter

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
//        rv_search_genre.addOnScrollListener(scrollListener)
    }


//    private fun loadMoreData() {
//        searchShowAdapter.addLoadingView()
//        //disini get data//
//        Handler().postDelayed({
//            viewModel.loadMore()
//            //end//
//            searchShowAdapter.removeLoadingView()
//            scrollListener.setLoaded()
//            rv_search_genre.post {
//                searchShowAdapter.notifyDataSetChanged()
//            }
//        }, 100)
//
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
