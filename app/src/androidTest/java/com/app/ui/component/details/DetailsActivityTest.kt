package com.app.ui.component.details

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.app.R
import com.app.RECIPE_ITEM_KEY
import com.app.TestUtil.initData
import com.app.TestUtil.recipes
import com.app.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.*

@HiltAndroidTest
class DetailsActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(DetailsActivity::class.java, true, false)
    private var mIdlingResource: IdlingResource? = null

    @Before
    fun setup() {
        initData()
        val intent: Intent = Intent().apply {
            putExtra(RECIPE_ITEM_KEY, recipes.recipesList[0])
        }
        mActivityTestRule.launchActivity(intent)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testRecipeNameDescriptionAndFavoriteView() {
        onView(withId(R.id.nameText)).check(matches(isDisplayed()))
        onView(withId(R.id.nameText)).check(matches(withText(recipes.recipesList[0].name)))
        onView(withId(R.id.descriptionText)).perform(scrollTo())
        onView(withId(R.id.descriptionText)).check(matches(withText(recipes.recipesList[0].description)))
        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.add_to_favorite)).check(matches(isClickable()))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}
