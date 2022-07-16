plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

repositories {
    mavenCentral()
}

apply(plugin = "com.vanniktech.maven.publish")

dependencies {
    // Kotlin Dependencies
    implementation(Dependencies.Kotlin.KOTLIN)
    implementation(Dependencies.Kotlin.KSP)
    implementation(Dependencies.Square.Poet.KOTLIN)
    implementation(Dependencies.Square.Poet.KSP)

    implementation(project(":enum-convertible-annotations"))

    testImplementation(TestDependencies.JUnit.JUNIT)
    testImplementation(TestDependencies.Google.TRUTH)
    testImplementation(TestDependencies.Misc.KOTLIN_COMPILE_TESTING)
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}

plugins.withId("com.vanniktech.maven.publish") {
    mavenPublish {
        sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
    }
}
