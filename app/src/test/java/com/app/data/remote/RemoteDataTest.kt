package com.app.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.data.dto.recipes.RecipesItem
import com.app.data.error.NETWORK_ERROR
import com.app.data.error.NO_INTERNET_CONNECTION
import com.app.data.remote.service.RecipesService
import com.app.utils.NetworkConnectivity
import com.util.InstantExecutorExtension
import com.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class RemoteDataTest {

    @MockK
    lateinit var serviceGenerator: ServiceGenerator

    @MockK
    lateinit var networkConnectivity: NetworkConnectivity

    @MockK
    lateinit var recipesService: RecipesService

    @MockK
    lateinit var response: Response<List<RecipesItem>>

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var remoteData: RemoteData

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        remoteData = RemoteData(serviceGenerator, networkConnectivity)
    }

    @Test
    fun `request recipes, return success response`() = runBlocking {
        val recipeList = arrayListOf(RecipesItem(id = "1234"))
        every { serviceGenerator.createService(RecipesService::class.java) } returns recipesService
        coEvery { recipesService.fetchRecipes() } returns response
        every { networkConnectivity.isConnected() } returns true
        every { response.code() } returns SUCCESS_CODE
        every { response.isSuccessful } returns true
        every { response.body() } returns recipeList

        val result = remoteData.requestRecipes().data
        assertNotNull(result)
        assertFalse(result?.recipesList.isNullOrEmpty())
    }

    @Test
    fun `request recipes when no internet connection, return error code`() = runBlocking {
        every { serviceGenerator.createService(RecipesService::class.java) } returns recipesService
        coEvery { recipesService.fetchRecipes() } returns response
        every { networkConnectivity.isConnected() } returns false

        val result = remoteData.requestRecipes().errorCode

        assertEquals(NO_INTERNET_CONNECTION, result)
    }

    @Test
    fun `request recipes, when request is unsuccessful, returns receive error response`() =
        runBlocking {
            every { serviceGenerator.createService(RecipesService::class.java) } returns recipesService
            coEvery { recipesService.fetchRecipes() } returns response
            every { networkConnectivity.isConnected() } returns true
            every { response.code() } returns REQUEST_ERROR_CODE
            every { response.isSuccessful } returns false

            val result = remoteData.requestRecipes().errorCode

            assertNotNull(result)
            assertEquals(REQUEST_ERROR_CODE, result)
        }

    @Test
    fun `request recipes, when throws exception, return receive error response`() = runBlocking {
        every { serviceGenerator.createService(RecipesService::class.java) } returns recipesService
        coEvery { recipesService.fetchRecipes() } returns response
        every { networkConnectivity.isConnected() } returns true
        every { response.code() } throws IOException()

        val result = remoteData.requestRecipes().errorCode

        assertNotNull(result)
        assertEquals(NETWORK_ERROR, result)
    }

    companion object {
        const val REQUEST_ERROR_CODE = 400
        const val SUCCESS_CODE = 200
    }
}
