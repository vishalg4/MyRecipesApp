package com.app

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.app.data.dto.recipes.Recipes
import com.app.data.dto.recipes.RecipesItem
import com.app.data.remote.moshiFactories.MyKotlinJsonAdapterFactory
import java.io.InputStream
import java.lang.reflect.Type

object TestUtil {
    var dataStatus: DataStatus = DataStatus.Success
    var recipes: Recipes = Recipes(arrayListOf())
    fun initData(): Recipes {
        val moshi = Moshi.Builder()
                .add(MyKotlinJsonAdapterFactory())
                .build()
        val type: Type = Types.newParameterizedType(List::class.java, RecipesItem::class.java)
        val adapter: JsonAdapter<List<RecipesItem>> = moshi.adapter(type)
        val jsonString = getJson("RecipesApiResponse.json")
        adapter.fromJson(jsonString)?.let {
            recipes = Recipes(ArrayList(it))
            return recipes
        }
        return Recipes(arrayListOf())
    }

    private fun getJson(path: String): String {
        val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
        return inputStream.bufferedReader().use { it.readText() }
    }
}
