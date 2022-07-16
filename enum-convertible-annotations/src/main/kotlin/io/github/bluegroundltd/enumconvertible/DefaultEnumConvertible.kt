package io.github.bluegroundltd.enumconvertible

/**
 * Defines that an enum entry of an enum class (which is annotated with [EnumConvertible]) that can
 * be as a default value during the conversions. This annotations can be used only once in an enum
 * class. For example, if we had the following enum class:
 *
 * ```
 *
 * @EnumConvertible
 * enum class DummyEnum(
 *      @EnumConvertibleKey
 *      val rawValue: String,
 *      val intValue: Int
 * ) {
 *      @DefaultEnumConvertible
 *      UNDEFINED(rawValue = "undefined", 0),
 *
 *      RANDOM_ENTRY(rawValue = "random", -1);
 * }
 *
 * ```
 *
 * after kotlin symbol processing the following class would be generated to help us with conversions:
 *
 * ```
 *
 * object DummyEnumMapper {
 *
 *      fun fromRawValue(rawValue: String): DummyEnum = DummyEnum.values()
 *              .find { it.value.equals(rawValue, ignoreCase = true) } ?: DummyEnum.UNDEFINED
 *
 * }
 *
 * ```
 */
@Target(AnnotationTarget.PROPERTY)
annotation class DefaultEnumConvertible
