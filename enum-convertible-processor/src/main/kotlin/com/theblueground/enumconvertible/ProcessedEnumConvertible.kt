package com.theblueground.enumconvertible

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.ClassName

/**
 * Keeps all the values that were processed from an [EnumConvertible] that are needed in order to
 * build a mapper for the annotated enum class.
 */
data class ProcessedEnumConvertible(
    val classType: ClassName,
    val containingFile: KSFile,
    val convertibleKeys: List<ProcessedEnumConvertibleKey>,
    val defaultValueName: String?
)

val ProcessedEnumConvertible.qualifiedName
    get() = this.classType.canonicalName

val ProcessedEnumConvertible.simpleName
    get() = this.classType.simpleName

val ProcessedEnumConvertible.packageName
    get() = this.classType.packageName
