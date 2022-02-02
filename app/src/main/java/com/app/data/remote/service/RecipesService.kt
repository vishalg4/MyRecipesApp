package com.app.data.remote.service

import com.app.data.dto.recipes.RecipesItem
import retrofit2.Response
import retrofit2.http.GET

/**
 * RecipesService class contain API call methods
 */
interface RecipesService {

    /**
     * fetches Recipe response from API
     * @return [Response] contains list of Recipes Item [RecipesItem]
     */
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<RecipesItem>>
}
