package com.app.data.error.mapper

import android.content.Context
import com.app.R
import com.app.data.error.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * ErrorMapper is an implementation of [ErrorMapperSource] which returns proper error
 * message for given error code.
 * @property [context] represent context application.
 */
class ErrorMapper @Inject constructor(@ApplicationContext val context: Context) :
    ErrorMapperSource {

    /**
     * Returns error message for given error code.
     * @param [errorId] represent error code.
     * */
    override fun getErrorString(errorId: Int): String {
        return context.getString(errorId)
    }

    /**
     * Contains error messages with respect to the error codes.
     * */
    override val errorsMap: Map<Int, String>
        get() = mapOf(
            Pair(NO_INTERNET_CONNECTION, getErrorString(R.string.no_internet)),
            Pair(NETWORK_ERROR, getErrorString(R.string.activity_recipes_list_network_error)),
            Pair(SEARCH_ERROR, getErrorString(R.string.activity_recipe_list_search_error))
        )
}
