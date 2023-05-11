package io.github.gouvinb.wwmaccompanist.logger.presentation

import io.github.gouvinb.wwmaccompanist.logger.data.api.Ansi
import io.github.gouvinb.wwmaccompanist.logger.data.api.uColored
import io.github.gouvinb.wwmaccompanist.logger.data.model.LoggerLevel
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printWithoutTag
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnAssert
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnDebug
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnError
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnErrorAndExit
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnInfo
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnWarning
import io.github.gouvinb.wwmaccompanist.logger.presentation.common.printlnWithoutTag
import kotlin.system.exitProcess

var log: Logger = Logger.default()
    private set

@ThreadLocal
private var isNotInitialized = true

class Logger private constructor(
    private val level: LoggerLevel = LoggerLevel.INFO,
    private val isColorEnable: Boolean = true,
) {
    fun print(block: Ansi.() -> String) = printWithoutTag(block, isColorEnable)

    fun println(block: Ansi.() -> String) = printlnWithoutTag(block, isColorEnable)

    /**
     * Output assert.
     *
     * @param message assert message.
     */
    fun assert(message: String) {
        if (level <= LoggerLevel.ASSERT || Platform.isDebugBinary) printlnAssert(message, isColorEnable)
    }

    /**
     * Output debug.
     *
     * @param message debug message.
     */
    fun debug(message: String) {
        if (level <= LoggerLevel.DEBUG || Platform.isDebugBinary) printlnDebug(message, isColorEnable)
    }

    /**
     * Output info.
     *
     * @param message info message.
     */
    fun info(message: String) {
        if (level <= LoggerLevel.INFO) printlnInfo(message, isColorEnable)
    }

    /**
     * Output warning.
     *
     * @param message warning message.
     */
    fun warning(message: String) {
        if (level <= LoggerLevel.WARNING) printlnWarning(message, isColorEnable)
    }

    /**
     * Outputs an error.
     *
     * @param message error message.
     */
    fun error(message: String) {
        if (level <= LoggerLevel.ERROR) printlnError(message, isColorEnable)
    }

    /**
     * Outputs an error message adding the usage information after it.
     *
     * @param message error message.
     * @param code exit code.
     */
    fun wtf(message: String, exitCode: Int = 127): Nothing {
        printlnErrorAndExit(uColored { italic(message) }, exitCode, isColorEnable)
    }

    companion object {
        private fun init(level: LoggerLevel = LoggerLevel.INFO, isColorEnable: Boolean = true): Logger {
            require(isNotInitialized) { "The logger can be initialized only once." }
            isNotInitialized = false
            log = Logger(level, isColorEnable)
            return log
        }

        fun tryInit(level: LoggerLevel = LoggerLevel.INFO, isColorEnable: Boolean = true): Logger {
            try {
                (init(level, isColorEnable))
            } catch (e: Exception) {
                println("Error: ${e.message ?: "The logger cannot be initialized."}")
                exitProcess(127)
            }
            return log
        }

        fun reset() {
            isNotInitialized = true
            log = Logger.default()
        }

        fun default() = Logger(LoggerLevel.WARNING, false)
    }
}
