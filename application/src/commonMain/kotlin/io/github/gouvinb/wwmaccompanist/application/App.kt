package io.github.gouvinb.wwmaccompanist.application

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.enum
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand
import io.github.gouvinb.wwmaccompanist.logger.data.model.LoggerLevel
import io.github.gouvinb.wwmaccompanist.logger.presentation.Logger
import io.github.gouvinb.wwmaccompanist.screenshot.command.ScreenshotCommand
import platform.posix.STDIN_FILENO
import platform.posix.isatty

/**
 * The main entry point of `wwma`.
 *
 * @property completionCommand The command used for generating shell completion scripts.
 * @property audioCommand The command for audio control.
 * @property backlightCommand The command for backlight control.
 */
class App : CliktCommand(
    name = NAME,
    epilog = "See '$NAME <command> --help' for more information on a specific command.",
    help = "A custom collection of tools for Wayland WM",
    helpTags = mapOf("version" to "0.4.0"),
) {
    private val loggerLevel by option("--verbose", "-v", help = "Set the verbose level")
        .enum<LoggerLevel>(key = { it.name.lowercase() })
        .default(LoggerLevel.INFO, defaultForHelp = "${LoggerLevel.INFO}".lowercase())

    private val quiet by option("--quiet", "-q", help = "Same as `--verbose quiet` or `-v quiet`")
        .flag(default = false)

    private val color by option("--color", help = "Enable color")
        .flag("--no-color", default = isatty(STDIN_FILENO) != 0, defaultForHelp = "${isatty(STDIN_FILENO) != 0}")

    private val completionCommand = CompletionCommand(name = "completion")

    private val audioCommand = AudioCommand()
    private val backlightCommand = BacklightCommand()
    private val screenshotCommand = ScreenshotCommand()

    init {
        context {
            helpFormatter = WwmAccompanistHelpFormatter
        }
        versionOption(helpTags["version"]!!)
        subcommands(
            audioCommand,
            backlightCommand,
            screenshotCommand,
            completionCommand,
        )
    }

    override fun aliases(): Map<String, List<String>> = mapOf(
        "generate-completion" to listOf(completionCommand.commandName),
    )

    override fun run() {
        when (quiet) {
            true -> Logger.tryInit(LoggerLevel.QUIET, color)
            else -> Logger.tryInit(loggerLevel, color)
        }
    }

    companion object {
        /**
         * The name of the 'wwma' tool.
         */
        const val NAME = "wwma"
    }
}
