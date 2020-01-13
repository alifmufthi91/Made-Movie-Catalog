package com.example.moviecatalogue_made_s2.Adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.ui.Fragment.AboutFragment
import com.example.moviecatalogue_made_s2.ui.Fragment.ListFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val TAB_TITLES = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3
    )

    override fun getItem(position: Int): Fragment {
        var fragment:Fragment
        fragment = when(position){
            2 -> AboutFragment()
            else -> ListFragment.newListPage(position + 1)
        }
        return fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }

}