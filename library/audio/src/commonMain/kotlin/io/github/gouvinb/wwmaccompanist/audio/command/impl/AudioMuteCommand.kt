package io.github.gouvinb.wwmaccompanist.audio.command.impl

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import io.github.gouvinb.wwmaccompanist.audio.command.AudioSubCommand

class AudioMuteCommand : AudioSubCommand(
    name = "mute",
    help = "Mute or restore the volume",
    helpTags = mapOf("version" to "0.1.0"),
) {
    val state by argument("state")
        .help("If true or false, then the sound is muted or restored. If the argument is not given then the sound will be switched.")
        .convert { it.toBoolean() }
        .optional()

    override fun run() {
        when (state) {
            null -> engine.toggleVolume()
            else -> engine.isMute = state!!
        }
    }
}
