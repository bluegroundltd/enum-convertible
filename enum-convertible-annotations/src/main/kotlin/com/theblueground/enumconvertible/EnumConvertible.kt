package com.theblueground.enumconvertible

/**
 * Defines an enum class from which we can generate a mapper to make conversions from plain values
 * to enum entries. The way we define these functions depends on [EnumConvertibleKey]. For example,
 * if we had the following enum class:
 *
 * ```
 *
 * @EnumConvertible
 * enum class DummyEnum {
 *
 *      FIRST_ENTRY,
 *
 *      SECOND_ENTRY;
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
 *      // Will contain the functions to convert from plain values to DummyEnum
 *
 * }
 *
 * ```
 *
 * As we can see, the naming convention for the generated mapper is the enum name with the `Mapper`
 * suffix. So, from `DummyEnum` the `DummyEnumMapper` will be generated.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class EnumConvertible
