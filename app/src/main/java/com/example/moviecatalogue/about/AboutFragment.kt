package com.example.moviecatalogue.about


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.R
import com.example.moviecatalogue.utils.GlideApp
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : DaggerFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlideApp.with(view.context)
            .load(BuildConfig.PROFILE_URL)
            .into(about_img)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }


}
