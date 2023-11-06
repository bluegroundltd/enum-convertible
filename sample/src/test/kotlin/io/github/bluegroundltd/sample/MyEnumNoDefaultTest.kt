package io.github.bluegroundltd.sample

import org.junit.Assert.assertEquals
import org.junit.Test

class MyEnumNoDefaultTest {

    @Test
    fun `should map unknown string value to null if no value is marked with @DefaultEnumConvertible`() {
        // Given
        val unknownValue = "unknown string value"

        // When
        val mappedValue = MyEnumNoDefaultMapper.fromValue(value = unknownValue)

        // Then
        assertEquals(null, mappedValue)
    }
}
