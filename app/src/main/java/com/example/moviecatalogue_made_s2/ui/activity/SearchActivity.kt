package com.example.moviecatalogue_made_s2.ui.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue_made_s2.R
import com.example.moviecatalogue_made_s2.ui.fragment.SearchMenuFragment
import com.example.moviecatalogue_made_s2.ui.fragment.SearchResultFragment
import com.example.moviecatalogue_made_s2.ui.viewmodel.SearchViewModel
import com.example.moviecatalogue_made_s2.ui.viewmodel.SearchViewModel.Companion.FIRST_PAGE

class SearchActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    private val mFragmentManager = supportFragmentManager
    private val mSearchResultFragment = SearchResultFragment()
    private val mSearchMenuFragment = SearchMenuFragment()
    private var fragment = mFragmentManager.findFragmentByTag(SearchMenuFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (fragment !is SearchResultFragment){
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mSearchMenuFragment, SearchMenuFragment::class.java.simpleName)
                .commit()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        searchViewModel = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.NewInstanceFactory()
        ).get(
            SearchViewModel::class.java
        )

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search_menu).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconified = false
            queryHint = getString(R.string.query_hint)
            onActionViewExpanded()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if(fragment !is SearchResultFragment){
                        fragment = mFragmentManager.findFragmentByTag(SearchResultFragment::class.java.simpleName)
                        mFragmentManager
                            .beginTransaction()
                            .replace(R.id.frame_container, mSearchResultFragment, SearchResultFragment::class.java.simpleName)
                            .commit()
                    }
                    searchViewModel.query = query
                    searchViewModel.setShows(searchViewModel.category, FIRST_PAGE, searchViewModel.query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()){
                        if(fragment !is SearchResultFragment){
                            fragment = mFragmentManager.findFragmentByTag(SearchResultFragment::class.java.simpleName)
                            mFragmentManager
                                .beginTransaction()
                                .replace(R.id.frame_container, mSearchResultFragment, SearchResultFragment::class.java.simpleName)
                                .commit()
                        }
                        searchViewModel.query = newText
                        searchViewModel.setShows(searchViewModel.category, FIRST_PAGE, searchViewModel.query)
                    }else{
                        if(fragment !is SearchMenuFragment){
                            fragment = mFragmentManager.findFragmentByTag(SearchMenuFragment::class.java.simpleName)
                            mFragmentManager
                                .beginTransaction()
                                .replace(R.id.frame_container, mSearchMenuFragment, SearchMenuFragment::class.java.simpleName)
                                .commit()
                        }
                    }
                    return false
                }
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }


}
