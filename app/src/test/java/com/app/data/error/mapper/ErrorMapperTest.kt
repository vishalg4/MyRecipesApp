package com.app.data.error.mapper

import android.content.Context
import com.app.R
import com.app.data.error.NO_INTERNET_CONNECTION
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ErrorMapperTest {
    private lateinit var errorMapper: ErrorMapper
    private lateinit var contextMock: Context

    @Before
    fun setup() {
        contextMock = mockk(relaxed = true)
        errorMapper = ErrorMapper(contextMock)
    }

    @Test
    fun `get error string when error code provided, returns expected error string`() {
        val expectedString = "Please check your internet connection"
        every { contextMock.getString(R.string.no_internet) } returns expectedString

        val actualResult = errorMapper.errorsMap[NO_INTERNET_CONNECTION]

        assertEquals(expectedString, actualResult)
    }
}
