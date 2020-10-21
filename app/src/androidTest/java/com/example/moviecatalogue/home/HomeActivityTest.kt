package com.example.moviecatalogue.home


import android.view.View
import androidx.appcompat.view.menu.ExpandedMenuView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.example.moviecatalogue.R
import com.google.android.material.tabs.TabLayout
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test

class HomeActivityTest{

    @get:Rule
    var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun loadDetailMovie() {
        Thread.sleep(2000)
        onView(withId(R.id.rv_movie)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.show_cover)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_release)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_genre)).check(matches(isDisplayed()))
    }

    @Test
    fun loadDetailTv() {
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(1))
        Thread.sleep(2000)
        onView(withId(R.id.rv_tv)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.show_cover)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_release)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_movie_genre)).check(matches(isDisplayed()))
    }

    @Test
    fun checkRecyclerViews() {
        onView(withId(R.id.rv_movie))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(1))
        onView(withId(R.id.rv_tv))
            .check(matches(isDisplayed()))
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.favorites))
            .perform(click())
        onView(withId(R.id.rv_favourite_movie))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tabs)).perform(selectTabAtPosition(1))
        onView(withId(R.id.rv_favourite_tv))
            .check(matches(isDisplayed()))
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}