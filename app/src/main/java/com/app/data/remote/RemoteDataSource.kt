package com.app.data.remote

import com.app.data.Resource
import com.app.data.dto.recipes.Recipes

/**
 * Classes that inherit this interface, implements methods to make remote calls.
 */
internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Recipes>
}
