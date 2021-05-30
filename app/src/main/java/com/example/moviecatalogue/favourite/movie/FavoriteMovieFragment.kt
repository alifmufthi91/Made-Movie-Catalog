package com.example.moviecatalogue.favourite.movie


import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.R
//import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_MOVIE_URI
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.shows.movie.MovieViewModel
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_favourite_movie_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : DaggerFragment() {

    @Inject
    lateinit var listViewModel: FavoriteMovieViewModel
    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var contentObserver: ContentObserver

    companion object {
        private const val EXTRA_STATE = "EXTRA_MOVIE_STATE"
        private const val DATA_OBSERVER = "MovieObserver"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_movie_list, container, false)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
////        outState.putParcelableArrayList(EXTRA_STATE, listShowAdapter.currentList)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_MOVIE)
        listViewModel.getFavoriteMovies().observe(viewLifecycleOwner, Observer {
            Log.d("favorite tvs: ", it.toString())
            if (it != null){
                listShowAdapter.submitList(it)
            }
        })
//
//        val handlerThread = HandlerThread(DATA_OBSERVER)
//        handlerThread.start()
//        val handler = Handler(handlerThread.looper)
//        contentObserver = object : ContentObserver(handler) {
//            override fun onChange(self: Boolean) {
//                loadShowsAsync()
//            }
//        }

//        context?.contentResolver?.registerContentObserver(CONTENT_MOVIE_URI, true, contentObserver)


//        if (savedInstanceState == null) {
//            loadShowsAsync()
//        } else {
//            val list = savedInstanceState.getParcelableArrayList<Show>(EXTRA_STATE)
//            if (list != null) {
//                listShowAdapter.setData(list)
//            }
//        }


    }


    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
        listShowAdapter.notifyDataSetChanged()
        rv_favourite_movie.layoutManager = LinearLayoutManager(activity)
        rv_favourite_movie.adapter = listShowAdapter

    }


}
