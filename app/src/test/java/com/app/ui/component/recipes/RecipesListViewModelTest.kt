package com.app.ui.component.recipes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.data.DataRepository
import com.app.data.Resource
import com.app.data.dto.recipes.Recipes
import com.app.data.dto.recipes.RecipesItem
import com.app.data.error.Error
import com.app.data.error.NETWORK_ERROR
import com.app.usecase.errors.ErrorManager
import com.util.InstantExecutorExtension
import com.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class RecipesListViewModelTest {
    private lateinit var recipesListViewModel: RecipesListViewModel

    @MockK
    private lateinit var dataRepository: DataRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        recipesListViewModel = RecipesListViewModel(dataRepository)
    }

    @Test
    fun `Is recipe list empty when repository gives recipe list, returns false`() {
        val recipesList = arrayListOf(RecipesItem())
        val recipes = Recipes(recipesList)
        coEvery { dataRepository.requestRecipes() } returns flow {
            emit(Resource.Success(recipes))
        }
        recipesListViewModel.getRecipes()
        recipesListViewModel.recipesLiveData.observeForever { }

        val isRecipeListEmpty = recipesListViewModel.recipesLiveData
            .value?.data?.recipesList.isNullOrEmpty()

        assertEquals(false, isRecipeListEmpty)
        assertEquals(recipes, recipesListViewModel.recipesLiveData.value?.data)
    }

    @Test
    fun `Is recipe list empty when repository gives empty recipe list, returns true`() {
        val recipes = Recipes(arrayListOf())
        coEvery { dataRepository.requestRecipes() } returns flow {
            emit(Resource.Success(recipes))
        }
        recipesListViewModel.getRecipes()
        recipesListViewModel.recipesLiveData.observeForever { }

        val isEmptyList = recipesListViewModel.recipesLiveData
            .value?.data?.recipesList.isNullOrEmpty()

        assertEquals(recipes, recipesListViewModel.recipesLiveData.value?.data)
        assertEquals(true, isEmptyList)
    }

    @Test
    fun `fetch recipe api when network request fails, returns network error`() {
        val error: Resource<Recipes> = Resource.DataError(NETWORK_ERROR)
        coEvery { dataRepository.requestRecipes() } returns flow {
            emit(error)
        }
        recipesListViewModel.getRecipes()
        recipesListViewModel.recipesLiveData.observeForever { }

        assertEquals(NETWORK_ERROR, recipesListViewModel.recipesLiveData.value?.errorCode)
    }

    @Test
    fun `search for recipe name when result found, returns recipe item`() {
        val recipesResponse = getRecipesResponse()
        val searchTitle = "recipe2"

        coEvery { dataRepository.requestRecipes() } returns flow {
            emit(Resource.Success(recipesResponse))
        }
        recipesListViewModel.getRecipes()

        recipesListViewModel.onSearchClick(searchTitle)
        recipesListViewModel.recipeSearchFound.observeForever { }

        assertEquals(recipesResponse.recipesList[1], recipesListViewModel.recipeSearchFound.value)
    }

    @Test
    fun `search for recipe name when result not found, returns error message`() {
        val recipesResponse = getRecipesResponse()
        val searchTitle = "*&*^%"

        coEvery { dataRepository.requestRecipes() } returns flow {
            emit(Resource.Success(recipesResponse))
        }
        recipesListViewModel.getRecipes()
        recipesListViewModel.onSearchClick(searchTitle)
        recipesListViewModel.noSearchFound.observeForever { }

        assertEquals(Unit, recipesListViewModel.noSearchFound.value)
    }

    @Test
    fun `show recipe details when recipe is selected, returns initialize recipe details live data`() {
        val recipeItem = RecipesItem()
        recipesListViewModel.openRecipeDetails(recipeItem)

        assertNotNull(recipesListViewModel.openRecipeDetails.value)
    }

    private fun getRecipesResponse(): Recipes =
        Recipes(
            arrayListOf(
                RecipesItem(name = "recipe1"),
                RecipesItem(name = "recipe2"),
                RecipesItem(name = "recipe3"),
                RecipesItem(name = "recipe4")
            )
        )
}
