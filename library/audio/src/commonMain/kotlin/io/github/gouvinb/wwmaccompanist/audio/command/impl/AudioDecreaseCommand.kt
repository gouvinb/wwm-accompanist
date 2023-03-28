package io.github.gouvinb.wwmaccompanist.audio.command.impl

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.validate
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand.Companion.VOLUME_MAX
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand.Companion.VOLUME_MIN
import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand.Companion.VOLUME_RANGE
import io.github.gouvinb.wwmaccompanist.audio.command.AudioSubCommand
import kotlin.math.max

class AudioDecreaseCommand : AudioSubCommand(
    name = "decrease",
    help = "Decrease the volume to a given percentage",
    helpTags = mapOf("version" to "0.1.0"),
) {
    private val value by argument("value")
        .help("$VOLUME_RANGE%")
        .convert { it.toInt() }
        .validate {
            require(it in VOLUME_RANGE) { "Must be set between $VOLUME_MIN and $VOLUME_MAX percent" }
        }

    override fun run() {
        engine.increaseVolume(max(engine.volume - value, VOLUME_MIN))
    }
}
