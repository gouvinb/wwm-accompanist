package io.github.gouvinb.wwmaccompanist.screenshot.command.impl

import io.github.gouvinb.wwmaccompanist.screenshot.command.ScreenshotSubCommand
import io.github.gouvinb.wwmaccompanist.screenshot.target.TargetActionType

class ScreenshotSwappyCommand : ScreenshotSubCommand(
    name = "swappy",
    help = "Save the screenshot to a regular file or '-' to pipe to Swappy",
    helpTags = mapOf("version" to "0.1.0"),
) {
    override fun run() {
        target.initData().takeScreenshot(TargetActionType.SWAPPY)
    }
}
