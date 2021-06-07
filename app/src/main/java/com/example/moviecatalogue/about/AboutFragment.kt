package com.example.moviecatalogue.about


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.databinding.FragmentAboutBinding
import dagger.android.support.DaggerFragment

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : DaggerFragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(view.context)
            .load(BuildConfig.PROFILE_URL)
            .into(binding.aboutImg)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
