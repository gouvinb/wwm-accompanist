package io.github.gouvinb.wwmaccompanist.application

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand

class App : CliktCommand(
    name = NAME,
    epilog = "See '$NAME <command> --help' for more information on a specific command.",
    help = "A custom collection of tools for Wayland WM",
) {

    init {
        subcommands(
            AudioCommand()
        )
    }

    override fun run() = Unit

    companion object {
        private const val TAG = "App"

        const val NAME = "wwma"
    }
}

