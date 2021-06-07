package com.example.moviecatalogue.search.menu


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.R
import com.example.moviecatalogue.data.source.local.entity.GenreEntity
import com.example.moviecatalogue.databinding.FragmentSearchMenuBinding
import com.example.moviecatalogue.search.SearchByGenreActivity
import com.example.moviecatalogue.search.SearchByGenreActivity.Companion.SELECTED_CATEGORY
import com.example.moviecatalogue.search.SearchByGenreActivity.Companion.SELECTED_GENRE
import com.example.moviecatalogue.search.SearchViewModel
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import com.example.moviecatalogue.utils.Constant.SHOW_TV
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchMenuFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SearchViewModel::class.java)
    }
    private lateinit var adapter: ArrayAdapter<String>
    private var _binding: FragmentSearchMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val customTv = super.getView(position, convertView, parent) as TextView
                    customTv.setTextColor(ContextCompat.getColor(context, R.color.textPrimary))
                    return customTv
                }
            }
        binding.lvGenre.adapter = adapter
        val observer = Observer<List<GenreEntity>> {
            if (it != null) {
                for (genre in it.toList()) {
                    adapter.add(genre.name)
                }
                adapter.notifyDataSetChanged()
            }
        }
        binding.rgCategory.setOnCheckedChangeListener { _, checkedId ->
            adapter.clear()
            when (checkedId) {
                R.id.radio_movie -> {
                    searchViewModel.apply {
                        setCategory(SHOW_MOVIE)
                        setGenres()
                        getGenres().observe(viewLifecycleOwner, observer)
                    }
                    Log.d("radio", "movie")
                }
                R.id.radio_tv -> {
                    searchViewModel.apply {
                        setCategory(SHOW_TV)
                        setGenres()
                        getGenres().observe(viewLifecycleOwner, observer)
                    }
                    Log.d("radio", "tv")
                }
            }
        }
        searchViewModel.apply {
            setGenres()
            getGenres().observeForever(observer)
        }
        binding.lvGenre.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = Intent(context, SearchByGenreActivity::class.java)
                intent.putExtra(SELECTED_CATEGORY, searchViewModel.getCategory())
                intent.putExtra(
                    SELECTED_GENRE,
                    searchViewModel.getGenres().value?.get(position)
                )
                startActivity(intent)
            }

    }

}
