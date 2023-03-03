package io.github.gouvinb.wwmaccompanist.gradle.project.utils

enum class SelectedTarget {
    ALL, JS, JVM, NATIVE;

    fun matchWith(target: SelectedTarget) = if (this == ALL) true else this == target

    companion object {
        fun getFromProperty() = SelectedTarget.valueOf(System.getProperty("target", "ALL"))
    }
}
