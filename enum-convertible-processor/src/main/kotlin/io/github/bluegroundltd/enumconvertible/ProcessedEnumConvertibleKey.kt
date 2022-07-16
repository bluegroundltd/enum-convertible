package io.github.bluegroundltd.enumconvertible

import com.squareup.kotlinpoet.TypeName

/**
 * Keeps all the values that were processed from an [EnumConvertibleKey] that are needed in order to
 * build a mapper's conversion function, for the annotated enum class's key.
 */
data class ProcessedEnumConvertibleKey(
    val keyName: String,
    val typeName: TypeName
)
