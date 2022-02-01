package com.app.data.error

import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class ErrorTest {

    @Test
    fun `test error code and description when throws exception, returns expected result`() {
        val exceptionMsg = "exception occurred"
        val error = Error(Exception(exceptionMsg))

        Assert.assertEquals(exceptionMsg, error.description)
        Assert.assertEquals(DEFAULT_ERROR, error.code)
    }

    @Test
    fun `test error code and description when provided error code and message, returns expected result`() {
        val expectedErrorCode = -1
        val expectedErrorMsg = "test error message"
        val error = Error(expectedErrorCode, expectedErrorMsg)

        Assert.assertEquals(expectedErrorMsg, error.description)
        Assert.assertEquals(expectedErrorCode, error.code)
    }
}
