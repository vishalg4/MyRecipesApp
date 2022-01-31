package com.app.ui.component.recipes

import android.view.KeyEvent
import android.widget.AutoCompleteTextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.app.DataStatus
import com.app.R
import com.app.TestUtil.dataStatus
import com.app.TestUtil.recipes
import com.app.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

@HiltAndroidTest
class RecipesListActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(RecipesListActivity::class.java, false, false)
    private var mIdlingResource: IdlingResource? = null

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun displayRecipesList() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list)).check(matches(isDisplayed()))
        onView(withId(R.id.loadingProgressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testRecipesListRefresh() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list)).check(matches(isDisplayed()))
        onView(withId(R.id.loadingProgressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.action_refresh)).perform(click())
        onView(withId(R.id.rv_recipes_list)).check(matches(isDisplayed()))
        onView(withId(R.id.loadingProgressBar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testRecipeListIsNotDisplayedWithNoData() {
        dataStatus = DataStatus.Fail
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loadingProgressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.noDataText)).check(matches(isDisplayed()))
    }


    @Test
    fun testSearchWithSuccessfulSearchResult() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.action_search)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText(recipes.recipesList[0].name))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.nameText)).check(matches(isDisplayed()))
        onView(withId(R.id.descriptionText)).perform(scrollTo())
        onView(withId(R.id.descriptionText)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchWithNoResult() {
        dataStatus = DataStatus.EmptyResponse
        val searchText = "Any text"
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.action_search)).perform(click())
        sleep(LENGTH_LONG * 2L)
        onView(isAssignableFrom(AutoCompleteTextView::class.java))
            .perform(typeText(searchText))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))
        sleep(Toast.LENGTH_SHORT / 2L)
        onView(withText(R.string.activity_recipe_list_search_error))
            .inRoot(
                RootMatchers.withDecorView(
                    CoreMatchers.not(CoreMatchers.`is`(mActivityTestRule.activity.window.decorView))
                )
            ).check(matches(isDisplayed()))
    }

    @Test
    fun testRecipeListOnItemClickEvent() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
        onView(withId(R.id.nameText)).check(matches(isDisplayed()))
        onView(withId(R.id.descriptionText)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecipeListScrollEvent() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
        onView(withId(R.id.rv_recipes_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    5,
                    click()
                )
            )
        onView(withId(R.id.nameText)).check(matches(isDisplayed()))
        onView(withId(R.id.descriptionText)).check(matches(isDisplayed()))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}
