package io.github.gouvinb.wwmaccompanist.screenshot.command.impl

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import io.github.gouvinb.wwmaccompanist.screenshot.command.ScreenshotSubCommand
import io.github.gouvinb.wwmaccompanist.screenshot.target.ScreenshotTarget.Companion.FILE_PATH
import io.github.gouvinb.wwmaccompanist.screenshot.target.TargetActionType

class ScreenshotSaveCommand : ScreenshotSubCommand(
    name = "save",
    help = "Save the screenshot to a regular file",
    helpTags = mapOf("version" to "0.1.0"),
) {
    private val outputPath by option("-o", "--output", help = "Output file ('-' for stdout)")
        .default(FILE_PATH)

    override fun run() {
        target
            .initData()
            .setOutputFilePath(outputPath)
            .takeScreenshot(TargetActionType.SAVE)
    }
}
