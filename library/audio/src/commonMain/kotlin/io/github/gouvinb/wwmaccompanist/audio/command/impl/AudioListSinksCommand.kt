package io.github.gouvinb.wwmaccompanist.audio.command.impl

import io.github.gouvinb.wwmaccompanist.audio.command.AudioSubCommand

class AudioListSinksCommand : AudioSubCommand(
    name = "sinks",
    help = "List sinks",
    helpTags = mapOf("version" to "0.1.0"),
) {
    override fun run() {
        echo(engine.listSinks().ifEmpty { null }?.map { it.value }?.joinToString("\n") ?: "No sink found.")
    }
}
