package io.github.bluegroundltd.enumconvertible

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * Responsible for generating a file that will contain a mapper for each enum class that was
 * annotated the [EnumConvertible] annotation. This class will use the [ProcessedEnumConvertible]s
 * that were produced from the [EnumConvertibleVisitor].
 */
@KotlinPoetKspPreview
class EnumConvertibleMapperGenerator(
    private val codeGenerator: CodeGenerator
) {

    companion object {

        private const val OUTPUT_CONVERTIBLE_MAPPER_FILENAME_SUFFIX = "Mapper"
    }

    fun generate(processed: ProcessedEnumConvertible) {
        val filename = processed.simpleName + OUTPUT_CONVERTIBLE_MAPPER_FILENAME_SUFFIX

        val mapper = TypeSpec.objectBuilder(name = filename)
            .addOriginatingKSFile(ksFile = processed.containingFile)
            .addFunctions(funSpecs = processed.toFunSpecs())
            .build()

        val fileSpecBuilder = FileSpec.builder(
            packageName = processed.packageName,
            fileName = filename
        ).addType(typeSpec = mapper)

        processed.defaultValueName?.let { fileSpecBuilder.addImport(processed.qualifiedName, it) }

        fileSpecBuilder.build().writeTo(codeGenerator = codeGenerator, aggregating = true)
    }

    private fun ProcessedEnumConvertible.toFunSpecs(): List<FunSpec> = this.convertibleKeys.map { key ->
        val functionName = "from${key.keyName.replaceFirstChar { it.uppercaseChar() }}"

        val statement = buildFunctionStatement(
            className = this.simpleName,
            keyName = key.keyName,
            defaultValueName = this.defaultValueName
        )

        FunSpec.builder(name = functionName)
            .addParameter(name = key.keyName, type = key.typeName.copy(nullable = true))
            .addStatement(format = statement)
            .returns(returnType = this.buildReturnType())
            .build()
    }

    private fun buildFunctionStatement(
        className: String,
        keyName: String,
        defaultValueName: String?
    ): String {
        val defaultValuePart = buildDefaultValuePart(defaultValueName = defaultValueName)
        return "return $className.values().find { it.$keyName.equals($keyName,ignoreCase = true) }$defaultValuePart"
    }

    private fun buildDefaultValuePart(
        defaultValueName: String?
    ): String = if (defaultValueName != null) {
        " ?: $defaultValueName"
    } else {
        ""
    }

    private fun ProcessedEnumConvertible.buildReturnType(): TypeName =
        this.classType.copy(nullable = this.defaultValueName == null)
}
