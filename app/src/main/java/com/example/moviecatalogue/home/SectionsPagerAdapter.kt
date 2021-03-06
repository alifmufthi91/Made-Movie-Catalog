package com.example.moviecatalogue.home

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.moviecatalogue.R
import com.example.moviecatalogue.about.AboutFragment
import com.example.moviecatalogue.shows.movie.MovieFragment
import com.example.moviecatalogue.shows.tv.TvFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    companion object {
        @StringRes
        private val TAB_TITLES =
            intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)
    }

    override fun getItem(position: Int): Fragment =
        when (position) {
            2 -> AboutFragment()
            1 -> TvFragment()
            else -> MovieFragment()
        }


    @Nullable
    override fun getPageTitle(position: Int): CharSequence =
        mContext.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = 3

}