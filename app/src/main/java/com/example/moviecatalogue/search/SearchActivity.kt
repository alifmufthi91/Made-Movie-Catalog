package com.example.moviecatalogue.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.moviecatalogue.R
import com.example.moviecatalogue.search.menu.SearchMenuFragment
import com.example.moviecatalogue.search.result.SearchResultFragment
import com.example.moviecatalogue.viewmodel.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SearchActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
    }
    private val mFragmentManager = supportFragmentManager
    private val mSearchResultFragment = SearchResultFragment()
    private val mSearchMenuFragment = SearchMenuFragment()
    private var fragment =
        mFragmentManager.findFragmentByTag(SearchMenuFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (fragment !is SearchResultFragment) {
            displaySearchMenu()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
                    if (fragment !is SearchResultFragment) {
                        displaySearchResult(query)
                    }
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        searchViewModel.setQuery(query)
                    } else {
                        if (fragment !is SearchMenuFragment) {
                            displaySearchMenu()
                        }
                    }
                    return false
                }
            })
        }
        return true
    }

    fun displaySearchResult(query: String) {
        fragment =
            mFragmentManager.findFragmentByTag(SearchResultFragment::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame_container,
                mSearchResultFragment,
                SearchResultFragment::class.java.simpleName
            )
            .commit()
        searchViewModel.setQuery(query)
    }

    fun displaySearchMenu() {
        fragment =
            mFragmentManager.findFragmentByTag(SearchMenuFragment::class.java.simpleName)
        mFragmentManager
            .beginTransaction()
            .replace(
                R.id.frame_container,
                mSearchMenuFragment,
                SearchMenuFragment::class.java.simpleName
            )
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }


}
