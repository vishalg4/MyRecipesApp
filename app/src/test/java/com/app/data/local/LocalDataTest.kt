package com.app.data.local

import android.content.SharedPreferences
import com.app.FAVOURITES_KEY
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocalDataTest {

    @MockK
    lateinit var sharedPreferences: SharedPreferences

    @MockK
    lateinit var editor: SharedPreferences.Editor

    lateinit var localData: LocalData

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        localData = LocalData(sharedPreferences)
    }

    @Test
    fun `get cached favourites when preference has favourites returns non empty result`() {
        val cashedRecipeSet = setOf(RECIPE_ID)
        every { sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) } returns cashedRecipeSet

        val result = localData.getCachedFavourites().data

        assertFalse(result.isNullOrEmpty())
    }

    @Test
    fun `get cached favourites when preference has no favourites, returns null or empty result`() {
        every { sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) } returns setOf()

        val result = localData.getCachedFavourites().data

        assertTrue(result.isNullOrEmpty())
    }

    @Test
    fun `is recipe favourite when present in preference, returns true`() {
        val recipeId = RECIPE_ID
        val cashedRecipeSet = setOf(recipeId)
        every { sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) } returns cashedRecipeSet

        val result = localData.isFavourite(recipeId).data

        assertEquals(true, result)
    }

    @Test
    fun `is recipe favourite when present in preference, returns false`() {
        val recipeId = RECIPE_ID
        every { sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) } returns setOf()

        val result = localData.isFavourite(recipeId).data

        assertEquals(false, result)
    }

    @Test
    fun `cache favourite when saved successfully in preference, returns true`() {
        val recipeIdSet = setOf(RECIPE_ID)
        every { sharedPreferences.edit() } returns editor
        every { editor.putStringSet(FAVOURITES_KEY, recipeIdSet) } returns editor
        every { editor.commit() } returns true

        val result = localData.cacheFavourites(recipeIdSet).data

        assertEquals(true, result)
    }

    @Test
    fun `cache favourite when not saved successfully in preference, returns false`() {
        val recipeIdSet = setOf(RECIPE_ID)
        every { sharedPreferences.edit() } returns editor
        every { editor.putStringSet(FAVOURITES_KEY, recipeIdSet) } returns editor
        every { editor.commit() } returns false

        val result = localData.cacheFavourites(recipeIdSet).data

        assertEquals(false, result)
    }

    @Test
    fun `remove from favourite when recipe present in preference and removed successfully, returns true`() {
        val recipeIdSet = mutableSetOf(RECIPE_ID)

        every { sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) } returns recipeIdSet
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() } returns editor
        every { editor.putStringSet(FAVOURITES_KEY, emptySet()) } returns editor
        every { editor.commit() } returns true

        val result = localData.removeFromFavourites(RECIPE_ID).data

        assertEquals(true, result)
    }

    @Test
    fun `remove from favourite when recipe present in preference and not removed successfully, returns false`() {
        val recipeIdSet = mutableSetOf(RECIPE_ID)

        every { sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) } returns recipeIdSet
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() } returns editor
        every { editor.putStringSet(FAVOURITES_KEY, emptySet()) } returns editor
        every { editor.commit() } returns false

        val result = localData.removeFromFavourites(RECIPE_ID).data

        assertEquals(false, result)
    }

    @Test
    fun `remove from favourite when recipe is not present in preference, returns false`() {
        val emptyFavouritePrefSet = mutableSetOf<String>()

        every {
            sharedPreferences.getStringSet(
                FAVOURITES_KEY,
                setOf()
            )
        } returns emptyFavouritePrefSet
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() } returns editor
        every { editor.putStringSet(FAVOURITES_KEY, emptySet()) } returns editor
        every { editor.commit() } returns false

        val result = localData.removeFromFavourites(RECIPE_ID).data

        assertEquals(false, result)
    }

    companion object {
        const val RECIPE_ID = "recipeId1"
    }
}