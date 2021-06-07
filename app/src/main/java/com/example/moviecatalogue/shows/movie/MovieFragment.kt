package com.example.moviecatalogue.shows.movie


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue.databinding.FragmentMovieListBinding
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import com.example.moviecatalogue.vo.Status
import dagger.android.support.DaggerFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : DaggerFragment() {

    @Inject
    lateinit var listViewModel: MovieViewModel
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var mLayoutManger: RecyclerView.LayoutManager
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
//    private lateinit var scrollListener: CustomRecyclerViewScrollListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        listViewModel.shows.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                when (response.status) {
                    Status.SUCCESS -> {
                        listShowAdapter.submitList(response.data)
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
        mLayoutManger = LinearLayoutManager(context)
        binding.rvMovie.layoutManager = mLayoutManger
        binding.rvMovie.adapter = listShowAdapter

//        scrollListener =
//            CustomRecyclerViewScrollListener(
//                mLayoutManger as LinearLayoutManager
//            )
//        scrollListener.setOnLoadMoreListener(object :
//            CustomRecyclerViewScrollListener.OnLoadMoreListener {
//            override fun onLoadMore() {
//                loadMoreData()
//            }
//        })
//        rv_movie.addOnScrollListener(scrollListener)
    }

//    private fun loadMoreData() {
//        Handler().postDelayed({
//            listViewModel.nextPage()
//            scrollListener.setLoaded()
//        }, 100)
//    }


}
