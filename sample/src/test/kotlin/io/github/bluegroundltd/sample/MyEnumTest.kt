package io.github.bluegroundltd.sample

import org.junit.Assert.assertEquals
import org.junit.Test

class MyEnumTest {

    @Test
    fun `should map string value to enum value`() {
        // Given
        val firstEntryValue = MyEnum.FIRST_ENTRY.value

        // When
        val firstEntryEnumValue = MyEnumMapper.fromValue(value = firstEntryValue)

        // Then
        assertEquals(MyEnum.FIRST_ENTRY, firstEntryEnumValue)
    }

    @Test
    fun `should map unknown string value to the enum value marked with @DefaultEnumConvertible`() {
        // Given
        val unknownValue = "unknown string value"

        // When
        val defaultValue = MyEnumMapper.fromValue(value = unknownValue)

        // Then
        assertEquals(MyEnum.SECOND_ENTRY, defaultValue)
    }
}
