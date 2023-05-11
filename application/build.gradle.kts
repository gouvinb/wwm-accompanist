import io.github.gouvinb.wwmaccompanist.gradle.project.utils.SelectedTarget
import io.github.gouvinb.wwmaccompanist.gradle.project.utils.SystemInfo.linuxTargets
import io.github.gouvinb.wwmaccompanist.gradle.project.utils.extenstion.configureOrCreateNativePlatforms
import io.github.gouvinb.wwmaccompanist.gradle.project.utils.extenstion.createSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform")

    id("io.github.gouvinb.wwmaccompanist.gradle.project.base.main")
    id("io.github.gouvinb.wwmaccompanist.gradle.project.base.dokka")
    id("io.github.gouvinb.wwmaccompanist.gradle.project.base.spotless.java")
}

group = "io.github.gouvinb.wwmaccompanist.application"
version = "0.3.0"

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
    configureOrCreateNativePlatforms(jsCompilerType = IR)
        .forEach { kotlinTarget ->
            when (kotlinTarget) {
                is KotlinNativeTarget -> kotlinTarget.apply {
                    binaries {
                        executable {
                            baseName = rootProject.name
                        }
                    }
                }
                else -> { /* no-op */ }
            }
        }

    sourceSets {
        val selectedTarget = SelectedTarget.getFromProperty()

        val commonMain by getting {
            dependencies {
                implementation(libs.clikt)

                implementation(projects.library.logger)

                implementation(projects.library.audio)
                implementation(projects.library.backlight)
                implementation(projects.library.bar)
                implementation(projects.library.launcher)
                implementation(projects.library.screenshot)
                implementation(projects.library.theme)
                implementation(projects.library.wallpaper)
            }
        }
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
