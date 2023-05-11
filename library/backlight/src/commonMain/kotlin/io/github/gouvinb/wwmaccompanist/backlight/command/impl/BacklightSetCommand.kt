package io.github.gouvinb.wwmaccompanist.backlight.command.impl

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.versionOption
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand.Companion.BACKLIGHT_MAX
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand.Companion.BACKLIGHT_MIN
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightCommand.Companion.BACKLIGHT_RANGE
import io.github.gouvinb.wwmaccompanist.backlight.command.BacklightSubCommand

class BacklightSetCommand : BacklightSubCommand(
    name = "set",
    help = "Set the backlight level to a given percentage",
    helpTags = mapOf("version" to "0.1.3"),
) {
    private val value by argument("value")
        .help("$BACKLIGHT_RANGE%")
        .convert { it.toInt() }
        .validate {
            require(it in BACKLIGHT_RANGE) { "Must be set between $BACKLIGHT_MIN and $BACKLIGHT_MAX percent" }
        }

    init {
        versionOption(helpTags["version"]!!)
    }

    override fun run() {
        super.run()
        engine.brightness = value
    }
}
