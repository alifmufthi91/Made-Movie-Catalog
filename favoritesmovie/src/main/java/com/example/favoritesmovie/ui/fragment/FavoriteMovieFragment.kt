package com.example.favoritesmovie.ui.fragment


import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.BaseColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoritesmovie.R
import com.example.favoritesmovie.adapter.ListShowAdapter
import com.example.favoritesmovie.db.DatabaseContract.AUTHORITY
import com.example.favoritesmovie.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_MOVIE_URI
import com.example.favoritesmovie.helper.MappingHelper
import com.example.favoritesmovie.model.Show
import com.example.favoritesmovie.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private lateinit var listShowAdapter: ListShowAdapter

    companion object {
        const val SHOW_MOVIE = "Movie"
        private const val EXTRA_STATE = "EXTRA_MOVIE_STATE"
        private const val DATA_OBSERVER = "MovieObserver"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listShowAdapter.getData())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_MOVIE)

        val handlerThread = HandlerThread(DATA_OBSERVER)
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadShowsAsync()
            }
        }

        context?.contentResolver?.registerContentObserver(CONTENT_MOVIE_URI, true, myObserver)


        if (savedInstanceState == null) {
            loadShowsAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Show>(EXTRA_STATE)
            if (list != null) {
                listShowAdapter.setData(list)
            }
        }


    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
        listShowAdapter.notifyDataSetChanged()
        rv_shows.layoutManager = LinearLayoutManager(activity)
        rv_shows.adapter = listShowAdapter

    }


    private fun loadShowsAsync() {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredShows = async(Dispatchers.IO) {
                    if (context?.contentResolver?.acquireContentProviderClient(AUTHORITY) == null){
                        activity.let {
                            (it as MainActivity).showWarningDialog()
                        }
                        ArrayList()
                    }else{
                        val cursor = context?.contentResolver?.query(
                            CONTENT_MOVIE_URI,
                            null,
                            null,
                            null,
                            "${BaseColumns._ID} ASC"
                        ) as Cursor
                        MappingHelper.mapCursorToArrayList(cursor)
                    }
                }
                val shows = deferredShows.await()
                if (shows.size > 0) {
                    listShowAdapter.setData(shows)
                } else {
                    listShowAdapter.setData(ArrayList())
                }
            }
    }

}
