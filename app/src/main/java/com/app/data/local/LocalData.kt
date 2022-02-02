package com.app.data.local

import android.content.SharedPreferences
import com.app.FAVOURITES_KEY
import com.app.data.Resource
import javax.inject.Inject


/**
 * LocalData class is used to retrieve, save and delete recipes from favourites Shared Preference.
 * @param [sharedPreferences] instance of [SharedPreferences] use to retrieve, save and delete
 * recipes from saved preference.
 */
class LocalData @Inject constructor(private val sharedPreferences: SharedPreferences) {

    /**
     * Returns [Set] of strings contains saved favourite recipe ids.
     */
    fun getCachedFavourites(): Resource<Set<String>> {
        return Resource.Success(sharedPreferences.getStringSet(FAVOURITES_KEY, setOf()) ?: setOf())
    }

    /**
     * Returns true/false if given recipe ID is favourites.
     * @param [id] represent recipe ID.
     */
    fun isFavourite(id: String): Resource<Boolean> {
        val cache = sharedPreferences.getStringSet(FAVOURITES_KEY, setOf<String>()) ?: setOf()
        return Resource.Success(cache.contains(id))
    }

    /**
     * Saves recipes in favourites Shared Preference and return true/false based
     * on success/failure status.
     * @param[ids] represents set of recipe ids.
     */
    fun cacheFavourites(ids: Set<String>): Resource<Boolean> {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet(FAVOURITES_KEY, ids)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }

    /**
     * Removes recipes from favourites Shared Preference for given recipe ID and return true/false
     * based on success/failure status.
     * @param [id] represent recipe ID.
     */
    fun removeFromFavourites(id: String): Resource<Boolean> {
        val set =
            sharedPreferences.getStringSet(FAVOURITES_KEY, mutableSetOf<String>())?.toMutableSet()
                ?: mutableSetOf()
        if (set.contains(id)) {
            set.remove(id)
        }
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        editor.commit()
        editor.putStringSet(FAVOURITES_KEY, set)
        editor.apply()
        val isSuccess = editor.commit()
        return Resource.Success(isSuccess)
    }
}

