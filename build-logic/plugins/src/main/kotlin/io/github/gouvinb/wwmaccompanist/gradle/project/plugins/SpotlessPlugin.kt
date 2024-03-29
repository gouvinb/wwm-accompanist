package io.github.gouvinb.wwmaccompanist.gradle.project.plugins

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

abstract class SpotlessPlugin(private val isAndroidProject: Boolean) : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.diffplug.spotless")
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<SpotlessExtension> {
                kotlin {
                    target("**/*.kt")
                    targetExclude("**/build/**/*.kt")
                    ktlint(libs.findVersion("ktlint").get().toString())
                        .editorConfigOverride(mapOf("ktlint_code_style" to if (isAndroidProject) "android" else "official"))
                    // licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
                }
                format("kts") {
                    target("**/*.kts")
                    targetExclude("**/build/**/*.kts")
                    // Look for the first line that doesn't have a block comment (assumed to be the license)
                    // licenseHeaderFile(rootProject.file("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
                }
                format("xml") {
                    target("**/*.xml")
                    targetExclude("**/build/**/*.xml")
                    // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
                    // licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
                }
            }
        }
    }
}

class SpotlessJavaPlugin : SpotlessPlugin(false)

class SpotlessAndroidPlugin : SpotlessPlugin(true)
