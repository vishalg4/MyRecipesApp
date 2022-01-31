package com.app.usecase.errors

import com.app.data.error.Error

interface ErrorUseCase {
    fun getError(errorCode: Int): Error
}
