package io.github.gouvinb.wwmaccompanist.application

import com.github.ajalt.clikt.completion.CompletionCommand
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand

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
) {
    private val completionCommand = CompletionCommand(name = "completion")

    private val audioCommand = AudioCommand()
    private val backlightCommand = BacklightCommand()

    init {
        subcommands(
            completionCommand,
            audioCommand,
            backlightCommand,
        )
    }

    override fun aliases(): Map<String, List<String>> = mapOf(
        "generate-completion" to listOf(completionCommand.commandName),
    )

    override fun run() = Unit

    companion object {
        /**
         * The name of the 'wwma' tool.
         */
        const val NAME = "wwma"
    }
}
