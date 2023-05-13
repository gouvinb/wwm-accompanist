buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.dokka.gradlePlugin)
        classpath(libs.spotless.gradlePlugin)
        classpath(libs.dependencies.versions.gradlePlugin)
        classpath(libs.dependencies.versions.update.gradlePlugin)
    }
}

plugins {
    `version-catalog`
    id("io.github.gouvinb.wwmaccompanist.gradle.project.plugins.dependencies")
}
