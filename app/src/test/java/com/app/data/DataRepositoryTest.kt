package com.app.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.data.dto.recipes.Recipes
import com.app.data.dto.recipes.RecipesItem
import com.app.data.local.LocalData
import com.app.data.remote.RemoteData
import com.util.InstantExecutorExtension
import com.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class DataRepositoryTest {

    @MockK
    lateinit var remoteData: RemoteData

    @MockK
    lateinit var localRepository: LocalData

    @MockK
    lateinit var iterator: MutableIterator<String>

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var dataRepository: DataRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dataRepository = DataRepository(remoteData, localRepository, Dispatchers.IO)
    }

    @Test
    fun `request recipes, returns non empty recipe list`() = runBlocking {
        val recipes = Recipes(arrayListOf(RecipesItem()))
        val recipeResource = Resource.Success(recipes)
        coEvery { remoteData.requestRecipes() } returns recipeResource

        dataRepository.requestRecipes().collect {
            assertNotNull(it.data)
            assertFalse(it.data?.recipesList.isNullOrEmpty())
        }
    }

    @Test
    fun `remove from favourite, when removed successfully, returns true`() = runBlocking {
        val recipeId = "123"
        every { localRepository.removeFromFavourites(recipeId) } returns Resource.Success(true)

        dataRepository.removeFromFavourite(recipeId).collect {
            assertNotNull(it.data)
            assertEquals(true, it.data)
        }
    }

    @Test
    fun `remove from favourite when removable is unsuccessfully, returns false`() = runBlocking {
        val recipeId = "123"
        every { localRepository.removeFromFavourites(recipeId) } returns Resource.Success(false)

        dataRepository.removeFromFavourite(recipeId).collect {
            assertNotNull(it.data)
            assertEquals(false, it.data)
        }
    }

    @Test
    fun `is favourite when recipe is favourite, returns true`() = runBlocking {
        val recipeId = "123"
        every { localRepository.isFavourite(recipeId) } returns Resource.Success(true)

        dataRepository.isFavourite(recipeId).collect {
            assertNotNull(it.data)
            assertEquals(true, it.data)
        }
    }

    @Test
    fun `is favourite when recipe is not favourite, returns false`() = runBlocking {
        val recipeId = "123"
        every { localRepository.isFavourite(recipeId) } returns Resource.Success(false)

        dataRepository.isFavourite(recipeId).collect {
            assertNotNull(it.data)
            assertEquals(false, it.data)
        }
    }
}
