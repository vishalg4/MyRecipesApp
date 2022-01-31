package com.app.ui.component.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.data.DataRepository
import com.app.data.Resource
import com.app.data.dto.recipes.RecipesItem
import com.util.InstantExecutorExtension
import com.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class DetailsViewModelTest {

    @MockK
    private lateinit var dataRepository: DataRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var detailsViewModel: DetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        detailsViewModel = DetailsViewModel(dataRepository)
    }

    @Test
    fun `initialize recipe details with recipe model, returns expected recipe data`() {
        val expectedResult = RecipesItem()
        detailsViewModel.recipeData.observeForever { }

        detailsViewModel.initRecipeData(expectedResult)

        val recipesData = detailsViewModel.recipeData.value
        assertEquals(expectedResult, recipesData)
    }

    @Test
    fun `is this favorite recipe when recipe is added as favorite, returns true`() {
        val expectedResult = true
        val recipesItem = RecipesItem(id = "123")
        coEvery { dataRepository.addToFavourite(recipesItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        detailsViewModel.initRecipeData(recipesItem)
        detailsViewModel.addToFavourites()
        detailsViewModel.isFavourite.observeForever { }

        val recipesData = detailsViewModel.isFavourite.value
        assertEquals(expectedResult, recipesData?.data)
    }

    @Test
    fun `is this favorite recipe when recipe is not added to favorite, returns false`() {
        val expectedResult = false
        val recipesItem = RecipesItem()
        coEvery { dataRepository.isFavourite(recipesItem.id) } returns flow {
            emit(Resource.Success(false))
        }
        detailsViewModel.initRecipeData(recipesItem)
        detailsViewModel.isFavourites()
        detailsViewModel.isFavourite.observeForever { }

        val recipesData = detailsViewModel.isFavourite.value
        assertEquals(expectedResult, recipesData?.data)
    }

    @Test
    fun `is this favorite recipe when recipe is not removed successfully from favorite, returns true`() {
        val expectedResult = true
        val recipesItem = RecipesItem(id = "123")
        coEvery { dataRepository.addToFavourite(recipesItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        coEvery { dataRepository.removeFromFavourite(recipesItem.id) } returns flow {
            emit(Resource.Success(false))
        }
        detailsViewModel.initRecipeData(recipesItem)
        detailsViewModel.addToFavourites()
        detailsViewModel.removeFromFavourites()
        detailsViewModel.isFavourite.observeForever { }

        val isRecipeFavourite = detailsViewModel.isFavourite.value
        assertEquals(expectedResult, isRecipeFavourite?.data)
    }

    @Test
    fun `is this favorite recipe when recipe is removed successfully from favorite, returns False`() {
        val expectedResult = false
        val recipesItem = RecipesItem(id = "123")
        coEvery { dataRepository.addToFavourite(recipesItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        coEvery { dataRepository.removeFromFavourite(recipesItem.id) } returns flow {
            emit(Resource.Success(true))
        }
        detailsViewModel.initRecipeData(recipesItem)
        detailsViewModel.addToFavourites()
        detailsViewModel.removeFromFavourites()
        detailsViewModel.isFavourite.observeForever { }

        val isRecipeFavourite = detailsViewModel.isFavourite.value
        assertEquals(expectedResult, isRecipeFavourite?.data)
    }
}
