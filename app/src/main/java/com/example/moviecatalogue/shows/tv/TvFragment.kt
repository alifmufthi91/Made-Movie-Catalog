package com.example.moviecatalogue.shows.tv


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue.databinding.FragmentTvListBinding
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import com.example.moviecatalogue.vo.Status
import dagger.android.support.DaggerFragment
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class TvFragment : DaggerFragment() {

    @Inject
    lateinit var listViewModel: TvViewModel
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var mLayoutManger: RecyclerView.LayoutManager
    private var _binding: FragmentTvListBinding? = null
    private val binding get() = _binding!!
//    private lateinit var scrollListener: CustomRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        listViewModel.shows.observe(viewLifecycleOwner, Observer { shows ->
            if (shows != null) {
                when (shows.status) {
                    Status.SUCCESS -> {
                        listShowAdapter.submitList(shows.data)
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_TV)
        mLayoutManger = LinearLayoutManager(context)
        binding.rvTv.layoutManager = mLayoutManger
        binding.rvTv.adapter = listShowAdapter

//        scrollListener = CustomRecyclerViewScrollListener(mLayoutManger as LinearLayoutManager)
//        scrollListener.setOnLoadMoreListener(object :
//            CustomRecyclerViewScrollListener.OnLoadMoreListener {
//            override fun onLoadMore() {
//                loadMoreData()
//            }
//        })
//        rv_tv.addOnScrollListener(scrollListener)
    }

//    private fun loadMoreData() {
//        Handler().postDelayed({
//            listViewModel.nextPage()
//            scrollListener.setLoaded()
//        }, 100)
//    }


}
