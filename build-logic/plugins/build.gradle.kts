plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "io.github.gouvinb.wwmaccompanist.gradle"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.dokka.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
}

gradlePlugin {
    plugins {
        // Main
        register("main") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.base.main"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.base.MainPlugin"
        }

        // Dokka
        register("dokka") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.base.dokka"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.base.DokkaPlugin"
        }

        // Spotless
        register("javaSpotless") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.base.spotless.java"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.base.SpotlessJavaPlugin"
        }
        register("androidSpotless") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.base.spotless.android"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.base.SpotlessAndroidPlugin"
        }
    }
}
