package io.github.gouvinb.wwmaccompanist.audio.command.impl

import com.github.ajalt.clikt.parameters.options.versionOption
import io.github.gouvinb.wwmaccompanist.audio.command.AudioSubCommand

class AudioListSinksCommand : AudioSubCommand(
    name = "sinks",
    help = "List sinks",
    helpTags = mapOf("version" to "0.1.1"),
) {
    init {
        versionOption(helpTags["version"]!!)
    }

    override fun run() {
        echo(engine.listSinks().ifEmpty { null }?.map { it.value }?.joinToString("\n") ?: "No sink found.")
    }
}
