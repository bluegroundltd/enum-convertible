package io.github.bluegroundltd.enumconvertible

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview

/**
 * Responsible for processing all enums that were annotated with the [EnumConvertible] annotation
 * and building the corresponding mappers. It will use an [EnumConvertibleVisitor] to extract the
 * necessary values from the enum class declaration and then will use an
 * [EnumConvertibleMapperGenerator] to generate the mappers.
 */
@KotlinPoetKspPreview
class EnumConvertibleProcessor(
    codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    companion object {

        private val ENUM_CONVERTIBLE_ANNOTATION_FULLY_QUALIFIED_NAME = EnumConvertible::class.java.name
    }

    private val enumConvertibleMapperGenerator =
        EnumConvertibleMapperGenerator(codeGenerator = codeGenerator)

    private val processedEnumConvertibles = mutableListOf<ProcessedEnumConvertible>()

    private val enumConvertibleVisitor = EnumConvertibleVisitor(
        processedEnumConvertibles = processedEnumConvertibles
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(
            annotationName = ENUM_CONVERTIBLE_ANNOTATION_FULLY_QUALIFIED_NAME
        )

        val annotationName = EnumConvertible::class.simpleName // For logging purposes

        symbols.filterIsInstance<KSClassDeclaration>()
            .filter { kSClassDeclaration -> kSClassDeclaration.validate() }
            .forEach { kSClassDeclaration ->
                // look browse class information via StringValueEnumVisitor
                kSClassDeclaration.accept(enumConvertibleVisitor, Unit)
                val className = kSClassDeclaration.simpleName.asString()
                logger.logging(
                    message = "The class $className with $annotationName annotation was processed"
                )
            }

        return emptyList()
    }

    override fun finish() {
        processedEnumConvertibles.forEach { enumConvertibleMapperGenerator.generate(processed = it) }
    }

    override fun onError() {
        super.onError()
        logger.error("Failed to process enums")
    }
}
