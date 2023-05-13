package io.github.gouvinb.wwmaccompanist.screenshot.target.impl

import com.github.ajalt.clikt.core.PrintMessage
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.screenshot.target.ScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.OutputScreenshotTarget.OutputTargetData
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToString
import io.github.gouvinb.wwmaccompanist.util.kommand.StdioImpl

/**
 * Represents a screenshot target that captures the focused output (display) of the user's system.
 */
class OutputScreenshotTarget : ScreenshotTarget<OutputTargetData>() {

    /**
     * The name of the currently focused output (display) on the user's system.
     */
    val output: String by lazy {
        jqCommand()
            .args("-r", ".name")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToString(
                stdin = StdioImpl.Pipe(
                    jqCommand()
                        .args("-r", ".[] | select(.focused)")
                        .also { log.debug(it.prompt()) }
                        .spawnStdoutToString(stdin = StdioImpl.Pipe(stdinStr = swaymsgGetOutputs()))
                        .catchMessageFailure { message -> throw PrintMessage(message) }
                        .getOrThrow(),
                ),
            )
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    /**
     * The geometry (size and position) of the screenshot area.
     */
    override val geometry = ""

    /**
     * The name of the output that the screenshot will be captured from.
     */
    override val what: String
        get() = output

    /**
     * Initializes the data for this screenshot target.
     *
     * @return This screenshot target object after initializing its data.
     */
    override fun initData(): OutputScreenshotTarget {
        data = OutputTargetData(output, what)
        return this
    }

    /**
     * Represents the target data required to capture a screenshot of the focused output.
     *
     * @property target The name of the output to capture the screenshot from.
     * @property what The name of the focused output.
     */
    data class OutputTargetData(
        override val target: String,
        override val what: String,
    ) : TargetData(target, what)
}
