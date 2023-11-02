package io.github.bluegroundltd.sample

import io.github.bluegroundltd.enumconvertible.DefaultEnumConvertible
import io.github.bluegroundltd.enumconvertible.EnumConvertible
import io.github.bluegroundltd.enumconvertible.EnumConvertibleKey

@EnumConvertible
enum class MyEnum(@EnumConvertibleKey val value: String) {
    FIRST_ENTRY("first"),

    @DefaultEnumConvertible
    SECOND_ENTRY("second")
}
