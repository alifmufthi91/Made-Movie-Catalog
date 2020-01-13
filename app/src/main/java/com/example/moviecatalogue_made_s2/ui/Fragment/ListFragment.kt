package com.example.moviecatalogue_made_s2.ui.Fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue_made_s2.Adapter.ListShowAdapter
import com.example.moviecatalogue_made_s2.Model.Show
import com.example.moviecatalogue_made_s2.Model.ShowList
import com.example.moviecatalogue_made_s2.Utils.MovieDB
import com.example.moviecatalogue_made_s2.R
import kotlinx.android.synthetic.main.fragment_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private var showList = ArrayList<Show>()
    private lateinit var listHeroAdapter: ListShowAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var category:String? = "movie"
        var index = 1
        if (getArguments() != null) {
            index = arguments?.getInt(ARG_LIST_CATEGORY_NUMBER, 0) as Int
            when(index) {
                1 -> category = "movie"
                2 -> category = "tv"
                else -> category = "movie"
            }
        }
        showRecyclerList()
        Log.d("listCategory",category)
        getDataFromAPI(category)

    }

    private fun getDataFromAPI(category:String?){
        val builder = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder.build()
        val movieDBClient = retrofit.create(MovieDB::class.java)
        val call = movieDBClient.showList(category,"19fcb7dafd82f7ba409f93a3018e1f76")
        call.enqueue(object : Callback<ShowList> {
            override fun onResponse(call: Call<ShowList>, response: Response<ShowList>) {
                var shows = response.body()
                showList.addAll(shows!!.list)
                listHeroAdapter.notifyDataSetChanged()
                Log.d("adapternya",listHeroAdapter.getItemId(2).toString())
                showList.forEach { (index)->Log.d("title $category",index) }
            }
            override fun onFailure(call: Call<ShowList>, t: Throwable) {
                Toast.makeText(context, "Error...!!!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showRecyclerList() {
        rv_shows.setHasFixedSize(false)
        rv_shows.layoutManager = LinearLayoutManager(activity)
        listHeroAdapter = ListShowAdapter(showList)
        rv_shows.adapter = listHeroAdapter
    }


}
