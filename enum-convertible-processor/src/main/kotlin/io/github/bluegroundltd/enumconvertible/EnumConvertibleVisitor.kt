package io.github.bluegroundltd.enumconvertible

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * Responsible for analyzing an enum that was annotated with the [EnumConvertible] annotation and
 * extracting all the needed values in order to generate the corresponding mapper. These values will
 * be stored in a [ProcessedEnumConvertible].
 */
class EnumConvertibleVisitor(
    private val processedEnumConvertibles: MutableList<ProcessedEnumConvertible>
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if (classDeclaration.classKind != ClassKind.ENUM_CLASS) {
            throw IllegalStateException(
                "${EnumConvertible::class.simpleName} can be used only in an enum class."
            )
        }

        val processedEnumConvertible = ProcessedEnumConvertible(
            classType = classDeclaration.toClassName(),
            containingFile = classDeclaration.containingFile!!,
            convertibleKeys = extractConvertibleKeys(classDeclaration = classDeclaration),
            defaultValueName = extractDefaultValueName(classDeclaration = classDeclaration)
        )
        processedEnumConvertibles.add(processedEnumConvertible)
    }

    private fun extractDefaultValueName(classDeclaration: KSClassDeclaration): String? {
        val enumEntries = classDeclaration.declarations
        val defaultEnumEntries = enumEntries.filter { enumEntry ->
            enumEntry.annotations
                .contains(annotationName = DefaultEnumConvertible::class.simpleName!!)
        }

        if (defaultEnumEntries.count() > 1) {
            throw IllegalStateException(
                "Multiple default values were defined in ${classDeclaration.simpleName.asString()}"
            )
        }

        return defaultEnumEntries.singleOrNull()?.simpleName?.asString()
    }

    private fun extractConvertibleKeys(
        classDeclaration: KSClassDeclaration
    ): List<ProcessedEnumConvertibleKey> = classDeclaration.primaryConstructor!!
        .parameters
        .filter { parameter ->
            parameter.annotations.contains(annotationName = EnumConvertibleKey::class.simpleName!!)
        }
        .map { extractConvertibleKey(parameter = it) }

    private fun extractConvertibleKey(parameter: KSValueParameter): ProcessedEnumConvertibleKey =
        ProcessedEnumConvertibleKey(
            keyName = parameter.name!!.asString(),
            typeName = parameter.type.toTypeName()

        )

    private fun Sequence<KSAnnotation>.contains(annotationName: String): Boolean =
        any { it.shortName.asString() == annotationName }
}
