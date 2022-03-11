package com.theblueground.enumconvertible

/**
 * Defines that a field of an enum class (which is annotated with [EnumConvertible]) that can be
 * used in order to convert a value of the same type with the field into an enum entry. For example,
 * if we had the following enum class:
 *
 * ```
 *
 * @EnumConvertible
 * enum class DummyEnum(
 *      @EnumConvertibleKey
 *      val rawValue: String,
 *      val intValue: Int
 * ) {
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
 *      fun fromRawValue(rawValue: String): DummyEnum? =
 *          DummyEnum.values().find { it.value.equals(rawValue, ignoreCase = true) }
 *
 * }
 *
 * ```
 *
 * As we can see, the naming convention for the generated functions is the the `from` prefix with
 * the name of the key. So, for the key `rawValue` the `fromRawValue` will be generated.
 */
@Target(AnnotationTarget.FIELD)
annotation class EnumConvertibleKey
