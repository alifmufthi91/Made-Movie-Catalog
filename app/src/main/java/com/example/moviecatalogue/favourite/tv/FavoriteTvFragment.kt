package com.example.moviecatalogue.favourite.tv


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
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.model.Show
import com.example.moviecatalogue.db.DatabaseContract.FavoritesColumns.Companion.CONTENT_TV_URI
import com.example.moviecatalogue.helper.MappingHelper
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.shows.tv.TvFragment.Companion.SHOW_TV
import kotlinx.android.synthetic.main.fragment_favourite_tv_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvFragment : Fragment() {

    private lateinit var listShowAdapter: ListShowAdapter
    private lateinit var contentObserver: ContentObserver

    companion object {
        private const val EXTRA_STATE = "EXTRA_TV_STATE"
        private const val DATA_OBSERVER = "TvObserver"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_tv_list, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listShowAdapter.getData())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_TV)

        val handlerThread = HandlerThread(DATA_OBSERVER)
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        contentObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadShowsAsync()
            }
        }

        context?.contentResolver?.registerContentObserver(
            CONTENT_TV_URI,
            true,
            contentObserver
        )


        if (savedInstanceState == null) {
            loadShowsAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Show>(EXTRA_STATE)
            if (list != null) {
                listShowAdapter.setData(list)
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        context?.contentResolver?.unregisterContentObserver(contentObserver)
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_TV)
        listShowAdapter.notifyDataSetChanged()
        rv_favourite_tv.layoutManager = LinearLayoutManager(activity)
        rv_favourite_tv.adapter = listShowAdapter

    }


    private fun loadShowsAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredShows = async(Dispatchers.IO) {
                val cursor = context?.contentResolver?.query(
                    CONTENT_TV_URI,
                    null,
                    null,
                    null,
                    "${BaseColumns._ID} ASC"
                ) as Cursor
                MappingHelper.mapCursorToArrayList(cursor)
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
