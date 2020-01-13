package com.example.moviecatalogue_made_s2


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    companion object {
        private val ARG_LIST_CATEGORY_NUMBER = "section_number"

        fun newListPage(index: Int): ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_LIST_CATEGORY_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


}
