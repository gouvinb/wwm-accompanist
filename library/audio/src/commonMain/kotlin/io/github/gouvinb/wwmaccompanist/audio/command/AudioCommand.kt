package io.github.gouvinb.wwmaccompanist.audio.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.choice
import io.github.gouvinb.wwmaccompanist.audio.command.impl.AudioDecreaseCommand
import io.github.gouvinb.wwmaccompanist.audio.command.impl.AudioIncreaseCommand
import io.github.gouvinb.wwmaccompanist.audio.command.impl.AudioListSinksCommand
import io.github.gouvinb.wwmaccompanist.audio.command.impl.AudioMuteCommand
import io.github.gouvinb.wwmaccompanist.audio.command.impl.AudioSetCommand
import io.github.gouvinb.wwmaccompanist.audio.engine.PactlEngine
import io.github.gouvinb.wwmaccompanist.audio.engine.PamixerEngine

/**
 * [AudioCommand] is a command to manage the sound of the Linux system. It contains the following subcommands:
 *
 * - [AudioSetCommand] to set a defined sound value
 * - [AudioMuteCommand] to mute or unmute the sound
 * - [AudioIncreaseCommand] to increase the sound value
 * - [AudioDecreaseCommand] to decrease the sound value
 * - [AudioListSinksCommand] to lists the available sinks for audio output
 */
class AudioCommand : CliktCommand(
    name = "audio",
    help = "Audio management",
    helpTags = mapOf("version" to "0.1.3"),
) {
    private val engine by option("-e", "--engine", help = "Client used to manage audio")
        .choice(
            "pamixer" to PamixerEngine(),
            "pactl" to PactlEngine(),
        )
        .required()

    private val sink by option("-s", "--sink", help = "Selected sink used")

    private val setCommand = AudioSetCommand()
    private val muteCommand = AudioMuteCommand()
    private val increaseCommand = AudioIncreaseCommand()
    private val decreaseCommand = AudioDecreaseCommand()
    private val listSinksCommand = AudioListSinksCommand()

    init {
        versionOption(helpTags["version"]!!)
        subcommands(
            setCommand,
            muteCommand,
            increaseCommand,
            decreaseCommand,
            listSinksCommand,
        )
    }

    override fun aliases(): Map<String, List<String>> = mapOf(
        "up" to listOf(increaseCommand.commandName),
        "add" to listOf(increaseCommand.commandName),

        "down" to listOf(decreaseCommand.commandName),
        "subtract" to listOf(decreaseCommand.commandName),

        "toggle" to listOf(muteCommand.commandName),
    )

    override fun run() {
        currentContext.obj = engine.also { it.sink = sink }
    }

    companion object {
        const val VOLUME_MIN = 0
        const val VOLUME_MAX = 150

        val VOLUME_RANGE = VOLUME_MIN..VOLUME_MAX
    }
}
