package io.github.gouvinb.wwmaccompanist.screenshot.target.impl

import com.github.ajalt.clikt.core.PrintMessage
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.screenshot.target.ScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.WindowScreenshotTarget.WindowTargetData
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToString
import io.github.gouvinb.wwmaccompanist.util.kommand.StdioImpl

class WindowScreenshotTarget : ScreenshotTarget<WindowTargetData>() {
    override val geometry: String by lazy {
        slurpCommand()
        .args("-r", ".name")
        .also { log.debug(it.prompt()) }
        .spawnStdoutToString(
            stdin = StdioImpl.Pipe(
                jqCommand()
                    .args("-r", """.. | select(.pid? and .visible?) | .rect | "\(.x),\(.y) \(.width)x\(.height)"""")
                    .spawnStdoutToString(stdin = StdioImpl.Pipe(stdinStr = swaymsgGetTree()))
                    .catchMessageFailure { message -> throw PrintMessage(message) }
                    .getOrThrow(),
            ),
        )
        .catchMessageFailure { message -> throw PrintMessage(message) }
        .getOrThrow()
        .takeIf { it.isNotEmpty() }
        ?: throw PrintMessage("Slurp without selecting the area.")
    }

    override val what: String
        get() = "Window"

    override fun initData(): WindowScreenshotTarget {
        data = WindowTargetData(geometry, what)
        return this
    }

    data class WindowTargetData(
        override val target: String,
        override val what: String,
    ) : TargetData(target, what)
}
