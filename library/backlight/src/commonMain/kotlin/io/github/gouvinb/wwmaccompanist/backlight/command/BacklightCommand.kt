package io.github.gouvinb.wwmaccompanist.backlight.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.choice
import io.github.gouvinb.wwmaccompanist.backlight.command.impl.BacklightDecreaseCommand
import io.github.gouvinb.wwmaccompanist.backlight.command.impl.BacklightIncreaseCommand
import io.github.gouvinb.wwmaccompanist.backlight.command.impl.BacklightListDevicesCommand
import io.github.gouvinb.wwmaccompanist.backlight.command.impl.BacklightSetCommand
import io.github.gouvinb.wwmaccompanist.backlight.engine.BrightnessctlEngine
import io.github.gouvinb.wwmaccompanist.backlight.engine.BrilloEngine
import io.github.gouvinb.wwmaccompanist.backlight.engine.LightEngine

/**
 * [BacklightCommand] is a command to manage the sound of the Linux system. It contains the following subcommands:
 *
 * - [BacklightSetCommand] to set a defined sound value
 * - [BacklightIncreaseCommand] to increase the sound value
 * - [BacklightDecreaseCommand] to decrease the sound value
 * - [BacklightListDevicesCommand] to lists the available sinks for audio output
 */
class BacklightCommand : CliktCommand(
    name = "backlight",
    help = "Backlight management",
    helpTags = mapOf("version" to "0.1.1"),
) {
    private val engine by option("-e", "--engine", help = "Client used to manage backlight")
        .choice(
            "brightnessctl" to BrightnessctlEngine(),
            "brillo" to BrilloEngine(),
            "light" to LightEngine(),
        )
        .required()

    private val device by option("-d", "--device", help = "Selected device")

    private val setCommand = BacklightSetCommand()
    private val increaseCommand = BacklightIncreaseCommand()
    private val decreaseCommand = BacklightDecreaseCommand()
    private val listDevicesCommand = BacklightListDevicesCommand()

    init {
        versionOption(helpTags["version"]!!)
        subcommands(
            setCommand,
            increaseCommand,
            decreaseCommand,
            listDevicesCommand,
        )
    }

    override fun aliases(): Map<String, List<String>> = mapOf(
        "up" to listOf(increaseCommand.commandName),
        "add" to listOf(increaseCommand.commandName),

        "down" to listOf(decreaseCommand.commandName),
        "subtract" to listOf(decreaseCommand.commandName),
    )

    override fun run() {
        currentContext.obj = engine.also { it.device = device }
    }

    companion object {
        const val BACKLIGHT_MIN = 0
        const val BACKLIGHT_MAX = 100

        val BACKLIGHT_RANGE = BACKLIGHT_MIN..BACKLIGHT_MAX
    }
}
