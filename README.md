<p align="center">
  <img src="https://github.com/bluegroundltd/enum-convertible/blob/master/images/logo.png" />
</p>

# EnumConvertible [![CI](https://github.com/bluegroundltd/enum-convertible/actions/workflows/ci_test.yaml/badge.svg?branch=master)](https://github.com/bluegroundltd/enum-convertible/actions/workflows/ci_test.yaml)

Enums is pretty basic data type that it used in almost every codebase.
When using enums, it is pretty common to have conversions from primitive values to enums and vice versa. This produces a lot of boilerplate code.
`EnumConvertible` is a library that was built with [KSP](https://kotlinlang.org/docs/ksp-overview.html) and helps to handle all these conversions without the boilerplate code.

## Installation
Before using this library, we must set up KSP in our project. We can follow the instructions [here](https://kotlinlang.org/docs/ksp-quickstart.html#use-your-own-processor-in-a-project). Then we must include the following dependency:

``` 
implementation("io.github.bluegroundltd:enum-convertible-annotations:1.0.3")
implementation("io.github.bluegroundltd:enum-convertible:1.0.3")
```

## How to
The usage of this library is pretty simple. We have available three annotations which will help us to autogenerate all the boilerplate code. The annotations are:
- `EnumConvertible`: It must be applied to an enum, an indicates to our KSP processor to generate a mapper for this enum.
- `EnumConvertibleKey`: It must be applied to an enum's field, and indicates to our KSP processor to generate a function for converting this field's type to the corresponding enum
- `DefaultEnumConvertible`: It must be applied to an enum's entry, and indicates to our KSP processor to provide this enum entry when conversion is not possible. This annotation is not mandatory, but if it is not used then the mapping functions will return nullable results.

For example if we have the following enum:
```kotlin
@EnumConvertible
enum class MyEnum(@EnumConvertibleKey val value: String) {
	FIRST_ENTRY("first"),

	@DefaultEnumConvertible
	SECOND_ENTRY("second")
}
```

Then the following mapper class will be generated:
```kotlin
object MyEnumMapper {

    fun fromValue(value: String?): MyEnum? = MyEnum.values()
            .find { it.value.equals(value, ignoreCase = true) } 
}
```

The mapper class will be placed into the same package as the enum's, and we can use it like that:
```kotlin
val stringValue = "someString"
val enumEntry = MyEnumMapper.fromValue(value = stringValue)
```
