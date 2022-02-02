package com.app.data.remote

import com.app.data.Resource
import com.app.data.dto.recipes.Recipes
import com.app.data.dto.recipes.RecipesItem
import com.app.data.error.NETWORK_ERROR
import com.app.data.error.NO_INTERNET_CONNECTION
import com.app.data.remote.service.RecipesService
import com.app.utils.NetworkConnectivity
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * RemoteData class is an implementation of [RemoteDataSource] class which is use to retrieve
 * recipes data from network API call.
 * @param [serviceGenerator] use to provide Retrofit client to make API call.
 * @param [networkConnectivity] use to check internet connectivity.
 */
class RemoteData @Inject constructor(
    private val serviceGenerator: ServiceGenerator,
    private val networkConnectivity: NetworkConnectivity
) : RemoteDataSource {

    /**
     * This method use to call fetchRecipes api and returns processed response.
     * @return [Resource] contains Recipes response.
     */
    override suspend fun requestRecipes(): Resource<Recipes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)

        return when (val response = processRequest(recipesService::fetchRecipes)) {
            is List<*> -> {
                Resource.Success(data = Recipes(response as ArrayList<RecipesItem>))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    /**
     * Private method which is use to process API call after checking internet connection.
     * @param [responseCall] is a lambda function.
     * @return [Any] which can be response body or error code.
     */
    private suspend fun processRequest(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }
}
