package com.example.moviecatalogue.shows.movie


import android.os.Bundle
import android.os.Handler
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
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var listViewModel: MovieViewModel
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
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
        listShowAdapter.notifyDataSetChanged()
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
        listViewModel = ViewModelProviders.of(this, viewModelFactory)[MovieViewModel::class.java]
    }

    private fun loadMoreData() {
        Handler().postDelayed({
            listViewModel.loadMore()
            scrollListener.setLoaded()
            rv_movie.post {
                listShowAdapter.notifyDataSetChanged()
            }
        }, 100)
    }


}
