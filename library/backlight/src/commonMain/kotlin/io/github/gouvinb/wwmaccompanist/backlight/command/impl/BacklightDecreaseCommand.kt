package io.github.gouvinb.wwmaccompanist.backlight.command.impl

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.validate
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand.Companion.BACKLIGHT_MAX
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand.Companion.BACKLIGHT_MIN
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand.Companion.BACKLIGHT_RANGE
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightSubCommand
import kotlin.math.max

class BacklightDecreaseCommand : BacklightSubCommand(
    name = "decrease",
    help = "Decrease the volume to a given percentage",
    helpTags = mapOf("version" to "0.1.0"),
) {
    private val value by argument("value")
        .help("$BACKLIGHT_RANGE")
        .convert { it.toInt() }
        .validate {
            require(it in BACKLIGHT_RANGE) { "Must be set between $BACKLIGHT_MIN and $BACKLIGHT_MAX percent" }
        }

    override fun run() {
        engine.decreaseBrightness(max(engine.brightness - value, BACKLIGHT_MIN))
    }
}
