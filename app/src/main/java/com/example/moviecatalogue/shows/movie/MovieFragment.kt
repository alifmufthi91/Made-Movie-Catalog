package com.example.moviecatalogue.shows.movie


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue.R
import com.example.moviecatalogue.listener.CustomRecyclerViewScrollListener
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.vo.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : DaggerFragment() {

    @Inject
    lateinit var listViewModel: MovieViewModel
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var mLayoutManger: RecyclerView.LayoutManager
    private lateinit var scrollListener: CustomRecyclerViewScrollListener

    companion object {
        const val SHOW_MOVIE = "Movie"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showRecyclerList()
        listViewModel.setShows()
        listViewModel.getShows().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                when (response.status) {
                    Status.SUCCESS -> {
                        listShowAdapter.submitList(response.data)
//                        listShowAdapter.notifyDataSetChanged()
                    }
                    else -> {}
                }
            }
        })
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
//        listShowAdapter.notifyDataSetChanged()
        mLayoutManger = LinearLayoutManager(context)
        rv_movie.layoutManager = mLayoutManger
        scrollListener =
            CustomRecyclerViewScrollListener(
                mLayoutManger as LinearLayoutManager
            )
        scrollListener.setOnLoadMoreListener(object :
            CustomRecyclerViewScrollListener.OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        rv_movie.addOnScrollListener(scrollListener)
        rv_movie.adapter = listShowAdapter
    }

    private fun loadMoreData() {
        Handler().postDelayed({
            listViewModel.loadMore()
            scrollListener.setLoaded()
//            rv_movie.post {
//                listShowAdapter.notifyDataSetChanged()
//            }
        }, 100)
    }


}
