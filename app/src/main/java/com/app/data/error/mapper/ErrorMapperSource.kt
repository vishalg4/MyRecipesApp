package com.app.data.error.mapper

/**
 * Classes that inherit this interface implements error message with respect to error code.
 */
interface ErrorMapperSource {
    fun getErrorString(errorId: Int): String
    val errorsMap: Map<Int, String>
}
