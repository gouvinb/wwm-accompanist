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
 * This class defines the basic functionality for audio management using `pamixer`.
 */
class BrightnessctlEngine : BacklightEngine {
    override val command
        get() = Command("brightnessctl")

    override var device: String? = null

    override var brightness: Int
        get() = prepareCommand("get")
            .requireOutputText()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .lines()
            .first()
            .toDouble()
            .run { (this / 255.0) * 100.0 }
            .roundToInt()
        set(value) = prepareCommand("set", "$value%")
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
        prepareCommand("-s")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun restore(value: Int) {
        prepareCommand("-r")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun listDevices(): Map<String, String> {
        return command.args("-l")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToLines()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .drop(1)
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .fold(mutableMapOf<String, String>() to null as String?) { (map, currentDevice), line ->
                if (line.startsWith("Device ")) {
                    val deviceName = line.substringAfter("Device '").substringBefore("'")
                    map to deviceName
                } else if (currentDevice != null && line.contains("Current brightness")) {
                    val brightnessValue = line.substringAfter("Current brightness: ").substringBefore(" ")
                    val brightnessPercent = line.substringAfter(" (").substringBefore(")")
                    map.apply {
                        put(currentDevice, "$brightnessPercent [$brightnessValue")
                    } to currentDevice
                } else if (currentDevice != null && line.contains("Max brightness")) {
                    val maxValue = line.substringAfter("Max brightness: ")
                    map.apply {
                        val currentValue = get(currentDevice)
                        if (currentValue != null) {
                            put(currentDevice, "$currentValue/$maxValue]")
                        }
                    } to currentDevice
                } else {
                    map to currentDevice
                }
            }
            .first
    }

    private fun prepareCommand(vararg args: String) = device?.let { command.args("-d", it, *args) } ?: command.args(*args)
}
