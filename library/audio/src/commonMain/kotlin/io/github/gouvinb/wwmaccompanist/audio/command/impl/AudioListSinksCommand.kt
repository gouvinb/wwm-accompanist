package io.github.gouvinb.wwmaccompanist.audio.command.impl

import com.github.ajalt.clikt.parameters.options.versionOption
import io.github.gouvinb.wwmaccompanist.audio.command.AudioSubCommand
import io.github.gouvinb.wwmaccompanist.logger.presentation.log

class AudioListSinksCommand : AudioSubCommand(
    name = "sinks",
    help = "List sinks",
    helpTags = mapOf("version" to "0.1.2"),
) {
    init {
        versionOption(helpTags["version"]!!)
    }

    override fun run() {
        log.println {
            engine.listSinks().ifEmpty { null }?.map { it.value }?.joinToString("\n") ?: "No sink found."
        }
    }
}
