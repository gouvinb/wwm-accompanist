package io.github.gouvinb.wwmaccompanist.logger.presentation.common

import io.github.gouvinb.wwmaccompanist.logger.data.api.Ansi
import io.github.gouvinb.wwmaccompanist.logger.data.api.uColored
import kotlin.system.exitProcess

fun printWithoutTag(block: Ansi.() -> String, isColorEnabled: Boolean = true) = print(uColored(isColorEnabled, block))

fun printlnWithoutTag(block: Ansi.() -> String, isColorEnabled: Boolean = true) = println(uColored(isColorEnabled, block))

/**
 * Output assert.
 *
 * @param message assert message.
 */
fun printlnAssert(message: String, isColorEnabled: Boolean = true) {
    println(
        uColored(isColorEnabled) {
            val tag = "Assert:"
            "${bold(tag)} ${italic(message.replace("\n", "\n${"".padEnd(tag.length + 1)}"))}"
        },
    )
}

/**
 * Output debug.
 *
 * @param message debug message.
 */
fun printlnDebug(message: String, isColorEnabled: Boolean = true) {
    println(
        uColored(isColorEnabled) {
            val tag = "Debug:"
            "${bold(tag)} ${cyan(message.replace("\n", "\n${"".padEnd(tag.length + 1)}"))}"
        },
    )
}

/**
 * Output info.
 *
 * @param message info message.
 */
fun printlnInfo(message: String, isColorEnabled: Boolean = true) {
    println(
        uColored(isColorEnabled) {
            val tag = "Info:"
            "${bold(tag)} ${blue(message.replace("\n", "\n${"".padEnd(tag.length + 1)}"))}"
        },
    )
}

/**
 * Output warning.
 *
 * @param message warning message.
 */
fun printlnWarning(message: String, isColorEnabled: Boolean = true) {
    println(
        uColored(isColorEnabled) {
            val tag = "Warning:"
            "${bold(tag)} ${purple(message.replace("\n", "\n${"".padEnd(tag.length + 1)}"))}"
        },
    )
}

/**
 * Outputs an error.
 *
 * @param message error message.
 */
fun printlnError(message: String, isColorEnabled: Boolean = true) {
    println(
        uColored(isColorEnabled) {
            val tag = "Error:"
            "${bold(tag)} ${red(message.replace("\n", "\n${"".padEnd(tag.length + 1)}"))}"
        },
    )
}

/**
 * Outputs an error message adding the usage information after it.
 *
 * @param message error message.
 * @param exitCode exit code.
 */
fun printlnErrorAndExit(message: String, exitCode: Int = 127, isColorEnabled: Boolean = true): Nothing {
    printlnError(message, isColorEnabled)
    exitProcess(exitCode)
}
