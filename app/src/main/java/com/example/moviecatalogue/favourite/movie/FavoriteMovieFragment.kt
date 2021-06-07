package com.example.moviecatalogue.favourite.movie


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.databinding.FragmentFavouriteMovieListBinding
import com.example.moviecatalogue.shows.ListShowAdapter
import com.example.moviecatalogue.utils.Constant.SHOW_MOVIE
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : DaggerFragment() {
    private var _binding: FragmentFavouriteMovieListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var listViewModel: FavoriteMovieViewModel
    private lateinit var listShowAdapter: ListShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showRecyclerList()
        Log.d("listCategory", SHOW_MOVIE)
        listViewModel.getFavoriteMovies().observe(viewLifecycleOwner, Observer {
            Log.d("favorite tvs: ", it.toString())
            if (it != null) {
                listShowAdapter.submitList(it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList() {
        listShowAdapter = ListShowAdapter(this, SHOW_MOVIE)
        listShowAdapter.notifyDataSetChanged()
        binding.rvFavouriteMovie.layoutManager = LinearLayoutManager(activity)
        binding.rvFavouriteMovie.adapter = listShowAdapter

    }


}
