package io.github.gouvinb.wwmaccompanist.screenshot.command.impl

import io.github.gouvinb.wwmaccompanist.screenshot.command.ScreenshotSubCommand
import io.github.gouvinb.wwmaccompanist.screenshot.target.TargetActionType

class ScreenshotCopyCommand : ScreenshotSubCommand(
    name = "copy",
    help = "Copy the screenshot data into the clipboard",
    helpTags = mapOf("version" to "0.1.0"),
) {
    override fun run() {
        target.initData().takeScreenshot(TargetActionType.COPY)
    }
}
