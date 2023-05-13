package io.github.gouvinb.wwmaccompanist.screenshot.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotCheckCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotCopyCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotSaveCommand
import io.github.gouvinb.wwmaccompanist.screenshot.command.impl.ScreenshotSwappyCommand

/**
 * [ScreenshotCommand] is a command to manage the sound of the Linux system. It contains the following subcommands:
 *
 * - [BacklightSetCommand] to set a defined sound value
 * - [BacklightIncreaseCommand] to increase the sound value
 * - [BacklightDecreaseCommand] to decrease the sound value
 * - [BacklightListDevicesCommand] to lists the available sinks for audio output
 */
class ScreenshotCommand : CliktCommand(
    name = "screenshot",
    help = "helper for screenshots",
    helpTags = mapOf("version" to "0.1.0"),
) {
    private val notify by option("-n", "--notify", help = "Send notification")
        .flag("--no-notify", default = false)

    private val checkCommand = ScreenshotCheckCommand()
    private val copyCommand = ScreenshotCopyCommand()
    private val saveCommand = ScreenshotSaveCommand()
    private val swappyCommand = ScreenshotSwappyCommand()

    init {
        versionOption(helpTags["version"]!!)
        subcommands(
            checkCommand,
            copyCommand,
            saveCommand,
            swappyCommand,
        )
    }

    override fun aliases(): Map<String, List<String>> = mapOf(
        "cp" to listOf(copyCommand.commandName),
    )

    override fun run() {
        currentContext.obj = notify
    }

    companion object {}
}
