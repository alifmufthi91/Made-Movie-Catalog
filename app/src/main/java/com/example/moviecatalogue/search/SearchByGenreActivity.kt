package com.example.moviecatalogue.search

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.model.Genre
import com.example.moviecatalogue.listener.CustomRecyclerViewScrollListener
import com.example.moviecatalogue.search.result.SearchResultFragment
import com.example.moviecatalogue.utils.Constant
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_search_by_genre.*

class SearchByGenreActivity : AppCompatActivity() {

    private lateinit var searchShowAdapter: SearchShowAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var scrollListener: CustomRecyclerViewScrollListener
    private lateinit var viewModel: SearchByGenreViewModel


    companion object {
        const val SELECTED_GENRE = "selectedGenre"
        const val SELECTED_CATEGORY = "selectedCategory"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_genre)

        search_result.text = getString(R.string.search_result)

        val category = intent.getStringExtra(SELECTED_CATEGORY)
        val genre = intent.getParcelableExtra<Genre>(SELECTED_GENRE)

        supportActionBar?.title = genre?.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(
            viewModelStore,
            factory
        )[SearchByGenreViewModel::class.java]

        viewModel.setCategory(category as String)
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


        rv_search_genre.layoutManager = mLayoutManager
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
        rv_search_genre.addOnScrollListener(scrollListener)
        rv_search_genre.adapter = searchShowAdapter

        viewModel.setGenre(genre?.id.toString())
        viewModel.setShows(category)
        viewModel.getShows().observe(this, Observer {
            if (it != null) {
                searchShowAdapter.setData(it)
            }
        })
    }


    private fun loadMoreData() {
        searchShowAdapter.addLoadingView()
        //disini get data//
        Handler().postDelayed({
            viewModel.loadMore()
            //end//
            searchShowAdapter.removeLoadingView()
            scrollListener.setLoaded()
            rv_search_genre.post {
                searchShowAdapter.notifyDataSetChanged()
            }
        }, 100)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
