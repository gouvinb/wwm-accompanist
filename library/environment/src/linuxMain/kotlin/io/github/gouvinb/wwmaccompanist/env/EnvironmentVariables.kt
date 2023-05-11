package io.github.gouvinb.wwmaccompanist.env

import kotlinx.cinterop.toKString
import platform.posix.getenv

actual open class EnvironmentVariables {
    actual val _env: String? = getenv("_")?.toKString()
    actual val path: String = getenv("PATH")?.toKString() ?: throw IllegalStateException("Cannot get `path` environment variable.")
    actual val shlvl: String = getenv("SHLVL")?.toKString() ?: throw IllegalStateException("Cannot get `shlvl` environment variable.")
    actual val home: String = getenv("HOME")?.toKString() ?: throw IllegalStateException("Cannot get `HOME` environment variable.")
    actual val pwd: String = getenv("PWD")?.toKString() ?: throw IllegalStateException("Cannot get `PWD` environment variable.")
    actual val user: String = getenv("USER")?.toKString() ?: throw IllegalStateException("Cannot get `USER` environment variable.")
}
