package io.github.gouvinb.wwmaccompanist.gradle.project.base

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

class MainPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_11.toString()
                    freeCompilerArgs += "-Xjvm-default=all"
                }
            }

            tasks.withType<JavaCompile> {
                options.encoding = StandardCharsets.UTF_8.toString()
                sourceCompatibility = JavaVersion.VERSION_11.toString()
                targetCompatibility = JavaVersion.VERSION_11.toString()
            }

            tasks.withType<Test> {
                testLogging {
                    events(TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                    exceptionFormat = TestExceptionFormat.FULL
                    showStandardStreams = false
                }
            }
        }
    }
}
