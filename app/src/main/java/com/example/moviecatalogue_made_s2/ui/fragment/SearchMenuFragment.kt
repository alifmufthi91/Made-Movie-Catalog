package com.example.moviecatalogue_made_s2.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.ui.activity.SearchByGenreActivity
import com.example.moviecatalogue_made_s2.ui.activity.SearchByGenreActivity.Companion.SELECTED_CATEGORY
import com.example.moviecatalogue_made_s2.ui.activity.SearchByGenreActivity.Companion.SELECTED_GENRE
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue_made_s2.ui.fragment.TvFragment.Companion.SHOW_TV
import com.example.moviecatalogue_made_s2.ui.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search_menu.*

/**
 * A simple [Fragment] subclass.
 */
class SearchMenuFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: ArrayAdapter<String>

    companion object {
        private const val CATEGORY_STATE = "categoryState"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(
            activity!!.viewModelStore,
            ViewModelProvider.NewInstanceFactory()
        ).get(
            SearchViewModel::class.java
        )

        adapter = object : ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val customTv = super.getView(position, convertView, parent) as TextView
                customTv.setTextColor(resources.getColor(R.color.textPrimary))
                return customTv
            }
        }
        lv_genre.adapter = adapter
        rg_category.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_movie -> {
                    searchViewModel.category = SHOW_MOVIE
                    searchViewModel.setGenres(searchViewModel.category)
                    Log.d("radio", "movie")
                }
                R.id.radio_tv -> {
                    searchViewModel.category = SHOW_TV
                    searchViewModel.setGenres(searchViewModel.category)
                    Log.d("radio", "tv")
                }
            }
        }
        searchViewModel.setGenres(searchViewModel.category)
        searchViewModel.getGenre().observe(this, Observer {
            if (it != null) {
                adapter.clear()
                for (genre in it) {
                    lv_genre.visibility = View.GONE
                    adapter.add(genre.name)
                    lv_genre.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        })
        lv_genre.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(context, SearchByGenreActivity::class.java)
                intent.putExtra(SELECTED_CATEGORY, searchViewModel.category)
                intent.putExtra(SELECTED_GENRE, searchViewModel.listGenre[position])
                startActivity(intent)
            }

    }

}
