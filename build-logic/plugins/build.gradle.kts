plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "io.github.gouvinb.wwmaccompanist.gradle"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.dokka.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    compileOnly(libs.dependencies.versions.gradlePlugin)
    compileOnly(libs.dependencies.versions.update.gradlePlugin)

}

gradlePlugin {
    plugins {
        // Main
        register("main") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.main"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.MainPlugin"
        }

        // Dokka
        register("dokka") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.dokka"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.DokkaPlugin"
        }

        // Spotless
        register("javaSpotless") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.spotless.java"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.SpotlessJavaPlugin"
        }
        register("androidSpotless") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.spotless.android"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.SpotlessAndroidPlugin"
        }

        // Version Catalog
        register("dependencies") {
            id = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.dependencies"
            implementationClass = "io.github.gouvinb.wwmaccompanist.gradle.project.plugins.DependenciesPlugin"
        }
    }
}
