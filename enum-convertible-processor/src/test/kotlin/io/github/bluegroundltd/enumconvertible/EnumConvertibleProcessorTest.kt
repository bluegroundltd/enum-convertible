package io.github.bluegroundltd.enumconvertible

import com.google.common.truth.Truth.assertThat
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.tschuchort.compiletesting.*
import com.tschuchort.compiletesting.SourceFile.Companion.kotlin
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@KotlinPoetKspPreview
class EnumConvertibleProcessorTest {

    private val generatedSourcesPath = "ksp/sources/kotlin/enum"

    @Rule
    @JvmField
    val temporaryFolder: TemporaryFolder = TemporaryFolder()

    private fun getGeneratedContent(filename: String): String {
        val path = temporaryFolder.root.resolve(generatedSourcesPath)
        val generatedFile = File(path, filename)
        return generatedFile.bufferedReader().readText()
    }

    @Test
    fun `should generate mapper`() {
        // Given
        val enumName = "BGEnum"
        val enumSource = """
                    package `enum`

                    import io.github.bluegroundltd.enumconvertible.EnumConvertible
                    import io.github.bluegroundltd.enumconvertible.EnumConvertibleKey

                    @EnumConvertible
                    enum class $enumName(@EnumConvertibleKey val `value`: String) {

                        FIRST_VALUE(`value` = "first"),

                        SECOND_VALUE(`value` = "second");
                    }
        """.trimIndent()
        val enumFile = kotlin(name = "$enumName.kt", contents = enumSource)

        // When
        val result = compile(enumFile)
        val generatedContent = getGeneratedContent(filename = "BGEnumMapper.kt")

        // Then
        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        val expectedSource = """
                    package `enum`
                    
                    import kotlin.String
                    
                    public object BGEnumMapper {
                      public fun fromValue(`value`: String?): BGEnum? = BGEnum.values().find { it.value.equals(value,
                          ignoreCase = true) } 
                    }
                    
                """.trimIndent()
        assertThat(generatedContent).isEqualTo(expectedSource)
    }

    @Test
    fun `should generate mapper with default value`() {
        // Given
        val enumName = "BGEnum"
        val enumSource = """
                    package `enum`

                    import io.github.bluegroundltd.enumconvertible.EnumConvertible
                    import io.github.bluegroundltd.enumconvertible.EnumConvertibleKey
                    import io.github.bluegroundltd.enumconvertible.DefaultEnumConvertible

                    @EnumConvertible
                    enum class $enumName(@EnumConvertibleKey val `value`: String) {

                        FIRST_VALUE(`value` = "first"),
    
                        @DefaultEnumConvertible
                        SECOND_VALUE(`value` = "second");
                    }
        """.trimIndent()
        val enumFile = kotlin(name = "$enumName.kt", contents = enumSource)

        // When
        val result = compile(enumFile)
        val generatedContent = getGeneratedContent(filename = "BGEnumMapper.kt")

        // Then
        assertThat(result.exitCode).isEqualTo(KotlinCompilation.ExitCode.OK)
        val expectedSource = """
                  package `enum`
                  
                  import `enum`.BGEnum.SECOND_VALUE
                  import kotlin.String
                  
                  public object BGEnumMapper {
                    public fun fromValue(`value`: String?): BGEnum = BGEnum.values().find { it.value.equals(value,
                        ignoreCase = true) } ?: SECOND_VALUE
                  }
                  
        """.trimIndent()
        assertThat(generatedContent).isEqualTo(expectedSource)
    }

    private fun prepareCompilation(vararg sourceFiles: SourceFile): KotlinCompilation = KotlinCompilation()
        .apply {
            workingDir = temporaryFolder.root
            inheritClassPath = true
            symbolProcessorProviders = listOf(EnumConvertibleProcessorProvider())
            sources = sourceFiles.asList()
            verbose = false
            kspWithCompilation = true
        }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result =
        prepareCompilation(*sourceFiles).compile()
}
