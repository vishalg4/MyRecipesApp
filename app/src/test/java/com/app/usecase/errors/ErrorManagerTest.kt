package com.app.usecase.errors

import com.app.data.error.mapper.ErrorMapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ErrorManagerTest {

    @MockK
    private lateinit var errorMapper: ErrorMapper

    private lateinit var errorManager: ErrorManager

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        errorManager = ErrorManager(errorMapper)
    }

    @Test
    fun `get error when provided error code, returns error message`() {
        val errorCode = -1
        val expectedMsg = "Network Error"
        val errorMap = mapOf(Pair(errorCode, expectedMsg))
        every { errorMapper.errorsMap } returns errorMap

        val error = errorManager.getError(errorCode)

        assertEquals(expectedMsg, error.description)
    }
}
