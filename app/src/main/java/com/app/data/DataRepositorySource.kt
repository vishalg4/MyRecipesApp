package com.app.data

import com.app.data.dto.recipes.Recipes
import kotlinx.coroutines.flow.Flow

/**
 * Classes that inherit this interface, implements methods to perform operation with Local and
 * remote data sources.
 */
interface DataRepositorySource {
    suspend fun requestRecipes(): Flow<Resource<Recipes>>
    suspend fun addToFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun isFavourite(id: String): Flow<Resource<Boolean>>
}
