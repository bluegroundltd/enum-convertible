package io.github.bluegroundltd.enumconvertible

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class EnumConvertibleProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        EnumConvertibleProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
}
