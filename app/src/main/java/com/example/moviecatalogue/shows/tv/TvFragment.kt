package com.example.moviecatalogue.shows.tv


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalogue.R
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import com.example.moviecatalogue.vo.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_tv_list.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class TvFragment : DaggerFragment() {

    @Inject
    lateinit var listViewModel: TvViewModel
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var mLayoutManger: RecyclerView.LayoutManager
//    private lateinit var scrollListener: CustomRecyclerViewScrollListener

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
        listViewModel.shows.observe(viewLifecycleOwner, Observer{ shows ->
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
        rv_tv.layoutManager = mLayoutManger
        rv_tv.adapter = listShowAdapter

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
