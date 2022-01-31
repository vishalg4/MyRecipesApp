package com.app.data.remote

import com.app.data.Resource
import com.app.data.dto.recipes.Recipes

internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Recipes>
}
