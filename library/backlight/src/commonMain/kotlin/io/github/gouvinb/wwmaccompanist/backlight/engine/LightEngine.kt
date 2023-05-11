package io.github.gouvinb.wwmaccompanist.backlight.engine

import com.github.ajalt.clikt.core.PrintMessage
import com.kgit2.process.Command
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.requireOutputText
import io.github.gouvinb.wwmaccompanist.util.extension.spawnCatching
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToLines
import kotlin.math.roundToInt

/**
 * This class defines the basic functionality for audio management using `pactl`.
 */
class LightEngine : BacklightEngine {
    override val command
        get() = Command("light")

    override var device: String? = null

    override var brightness: Int
        get() = prepareCommand("-G")
            .requireOutputText()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .lines()
            .first()
            .toDouble()
            .roundToInt()
        set(value) = prepareCommand("-S", "$value")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()

    override fun increaseBrightness(value: Int) {
        brightness = value
    }

    override fun decreaseBrightness(value: Int) {
        brightness = value
    }

    override fun save(value: Int) {
        prepareCommand("-O")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun restore(value: Int) {
        prepareCommand("-I")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun listDevices(): Map<String, String> {
        command.args("-L")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToLines()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .filter { it.isNotBlank() }

        return mapOf()
    }

    private fun prepareCommand(vararg args: String) =
        device?.let { command.args("-s", it, *args) } ?: command.args(*args)
}
