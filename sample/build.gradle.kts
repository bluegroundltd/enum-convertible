plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin Dependencies
    implementation(Dependencies.Kotlin.KOTLIN)
    implementation(Dependencies.Kotlin.KSP)

    implementation(project(":enum-convertible-annotations"))
    ksp(project(":enum-convertible-processor"))

    testImplementation(TestDependencies.JUnit.JUNIT)
}
