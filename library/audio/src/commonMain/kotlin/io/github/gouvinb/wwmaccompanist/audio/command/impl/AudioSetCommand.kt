package io.github.gouvinb.wwmaccompanist.audio.command.impl

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.validate
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand.Companion.VOLUME_RANGE
import io.github.gouvinb.wwmaccompanist.audio.command.AudioSubCommand

class AudioSetCommand : AudioSubCommand(
    name = "set",
    help = "Set the volume to a given percentage",
    helpTags = mapOf("version" to "0.1.0"),
) {
    private val value by argument("value")
        .help("0..150%")
        .convert { it.toInt() }
        .validate {
            require(it in VOLUME_RANGE) { "Must be set between 0 and 150 percent" }
        }

    override fun run() {
        engine.volume = value
    }
}
