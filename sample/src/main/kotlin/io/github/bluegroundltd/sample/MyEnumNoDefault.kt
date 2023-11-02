package io.github.bluegroundltd.sample

import io.github.bluegroundltd.enumconvertible.EnumConvertible
import io.github.bluegroundltd.enumconvertible.EnumConvertibleKey

@EnumConvertible
enum class MyEnumNoDefault(@EnumConvertibleKey val value: String) {
    FIRST_ENTRY("first"),

    SECOND_ENTRY("second")
}
