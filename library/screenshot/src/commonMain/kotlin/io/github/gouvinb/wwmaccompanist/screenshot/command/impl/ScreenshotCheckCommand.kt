package io.github.gouvinb.wwmaccompanist.screenshot.command.impl

import com.github.ajalt.clikt.core.Abort
import io.github.gouvinb.wwmaccompanist.env.EnvPath
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.screenshot.command.ScreenshotSubBaseCommand

class ScreenshotCheckCommand : ScreenshotSubBaseCommand(
    name = "check",
    help = "Verify if required tools are installed and exit",
    helpTags = mapOf("version" to "0.1.0"),
) {
    override fun run() {
        listOf(
            isCommandAvailable("grim"),
            isCommandAvailable("slurp"),
            isCommandAvailable("swaymsg"),
            isCommandAvailable("wl-copy"),
            isCommandAvailable("jq"),
            isCommandAvailable("notify-send"),
            isCommandAvailable("swappy"),
        ).onEach { (binaryName, isNotAvailable) ->
            log.println { "${if (isNotAvailable) "\u274c" else "\u2714"}  ${bold("`$binaryName`")}" }
        }.filter { (_, isNotAvailable) ->
            isNotAvailable
        }.takeIf {
            it.isNotEmpty()
        }?.also {
            log.println {
                "You can install this command if it is not already done and check your ${bold(italic("`PATH`"))} to solve this problem."
            }
            throw Abort()
        }
        log.println { "You can take a screenshot." }
    }

    private fun isCommandAvailable(binaryName: String) = binaryName to (binaryName !in EnvPath)
}
