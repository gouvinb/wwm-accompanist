package io.github.gouvinb.wwmaccompanist.audio.engine

import com.github.ajalt.clikt.core.PrintMessage
import com.kgit2.process.Command
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.requireOutputText
import io.github.gouvinb.wwmaccompanist.util.extension.spawnCatching
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToLines

/**
 * This class defines the basic functionality for audio management using `pamixer`.
 */
class PamixerEngine : AudioEngine {
    override val command
        get() = Command("pamixer")

    override var sink: String? = null

    override var volume: Int
        get() = command.args("--get-volume")
            .requireOutputText()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .toInt()
        set(value) = command.args("--allow-boost", "--set-volume", "$value")
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()

    override var isMute: Boolean
        get() = command.args("--get-mute")
            .requireOutputText()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .toBooleanStrict()
        set(value) = command.args(if (value) "--mute" else "--unmute")
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()

    override fun toggleVolume() {
        command.args("--toggle-mute")
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun increaseVolume(value: Int) {
        command.args("--allow-boost", "--increase", "$value")
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun decreaseVolume(value: Int) {
        command.args("--allow-boost", "--decrease", "$value")
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun listSinks() =
        command.args("--list-sinks")
            .spawnStdoutToLines()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .filter { it.isNotBlank() }
            .drop(1)
            .map { it.split(" ")[1].drop(1).dropLast(1) }
            .mapIndexed { i, it -> "$i" to it }
            .toMap()
}
