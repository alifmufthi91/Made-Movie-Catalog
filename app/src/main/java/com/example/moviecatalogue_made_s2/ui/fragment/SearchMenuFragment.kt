package com.example.moviecatalogue_made_s2.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.ui.fragment.MovieFragment.Companion.SHOW_MOVIE
import com.example.moviecatalogue_made_s2.ui.fragment.TvFragment.Companion.SHOW_TV
import com.example.moviecatalogue_made_s2.ui.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search_menu.*

/**
 * A simple [Fragment] subclass.
 */
class SearchMenuFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

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

        rg_category.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radio_movie -> {
                    searchViewModel.category = SHOW_MOVIE
                    Log.d("radio","movie")
                }
                R.id.radio_tv -> {
                    searchViewModel.category = SHOW_TV
                    Log.d("radio","tv")
                }
            }
        }
    }

}
