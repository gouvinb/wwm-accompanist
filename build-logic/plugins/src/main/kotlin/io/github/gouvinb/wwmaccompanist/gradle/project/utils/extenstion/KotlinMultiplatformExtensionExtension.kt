package io.github.gouvinb.wwmaccompanist.gradle.project.utils.extenstion

import io.github.gouvinb.wwmaccompanist.gradle.project.utils.SelectedTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

@Suppress("unused")
fun KotlinMultiplatformExtension.configureOrCreateNativePlatforms() = ArrayList<KotlinTarget>().apply {
    val selectedTarget = SelectedTarget.getFromProperty()

    if (selectedTarget.matchWith(SelectedTarget.NATIVE)) {
        // Required to generate tests tasks: https://youtrack.jetbrains.com/issue/KT-26547
        linuxX64().apply { add(this) }
    }
}
