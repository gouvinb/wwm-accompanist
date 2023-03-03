import io.github.gouvinb.wwmaccompanist.gradle.project.utils.SelectedTarget
import io.github.gouvinb.wwmaccompanist.gradle.project.utils.SystemInfo.linuxTargets
import io.github.gouvinb.wwmaccompanist.gradle.project.utils.extenstion.configureOrCreateNativePlatforms
import io.github.gouvinb.wwmaccompanist.gradle.project.utils.extenstion.createSourceSet

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")

    id("io.github.gouvinb.wwmaccompanist.gradle.project.base.main")
    id("io.github.gouvinb.wwmaccompanist.gradle.project.base.dokka")
    id("io.github.gouvinb.wwmaccompanist.gradle.project.base.spotless.java")
}

group = "io.github.gouvinb.wwmaccompanist.command.wallpaper"
version = "0.1.0"

repositories {
    mavenCentral()
    google()
}

/*
 * Here's the main hierarchy of variants. Any `expect` functions in one level of the tree are
 * `actual` functions in a (potentially indirect) child node.
 *
 * ```
 *   common
 *   '-- native
 *       '- unix
 *           '-- linux
 *               '-- linuxX64
 * ```
 *
 * The `nonJvm` source set excludes that platform.
 *
 * The `hashFunctions` source set builds on all platforms. It ships as a main source set on non-JVM
 * platforms and as a test source set on the JVM platform.
 */
kotlin {
    configureOrCreateNativePlatforms()

    sourceSets {
        val selectedTarget = SelectedTarget.getFromProperty()

        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val hashFunctions by creating {
            dependsOn(commonMain)
        }

        val nonAppleMain by creating {
            dependsOn(hashFunctions)
        }

        val nonJvmMain by creating {
            dependsOn(hashFunctions)
            dependsOn(commonMain)
        }
        val nonJvmTest by creating {
            dependsOn(commonTest)
        }

        if (selectedTarget.matchWith(SelectedTarget.NATIVE)) {
            createSourceSet("nativeMain", parent = nonJvmMain) { nativeMain ->
                createSourceSet("unixMain", parent = nativeMain) { unixMain ->
                    createSourceSet("linuxMain", parent = unixMain, children = linuxTargets) { linuxMain ->
                        linuxMain.dependsOn(nonAppleMain)
                    }
                }
            }

            createSourceSet("nativeTest", parent = commonTest, children = linuxTargets) { nativeTest ->
                nativeTest.dependsOn(nonJvmTest)
            }
        }
    }
}
