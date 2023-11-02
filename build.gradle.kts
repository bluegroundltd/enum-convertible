import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.diffplug.gradle.spotless.SpotlessPlugin

buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.9.10")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.25.3")
    }
}

plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("com.diffplug.spotless")
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val projectJvmTarget = JavaVersion.VERSION_17.toString()

subprojects {
    pluginManager.configureSpotlessIntegration(subProject = project)

    tasks.withType<KotlinCompile>().configureEach {
        dependsOn("spotlessKotlinApply")

        kotlinOptions {
            jvmTarget = projectJvmTarget
            languageVersion = "1.9"
        }
    }
}

fun PluginManager.configureSpotlessIntegration(subProject: Project) = apply {
    val spotlessConfiguration: (AppliedPlugin) -> Unit = {
        subProject.pluginManager.apply(SpotlessPlugin::class.java)
        subProject.configure<SpotlessExtension> {
            kotlin {
                target("src/**/*.kt")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
            }

            kotlinGradle {
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
            }
        }
    }

    withPlugin("org.jetbrains.kotlin.jvm", spotlessConfiguration)
}