package io.github.gouvinb.wwmaccompanist.audio.engine

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
class PactlEngine : AudioEngine {
    override val command
        get() = Command("pactl")

    override var sink: String? = null

    override var volume: Int
        get() = command.args("get-sink-volume", sink ?: DEFAULT_SINK)
            .requireOutputText()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .lines()
            .first()
            .run {
                FRONT_VOLUME_REGEX
                    .findAll(this)
                    .map { it.groupValues.last() }
                    .map { it.toInt() }
                    .average()
                    .roundToInt()
            }
        set(value) = command.args("set-sink-volume", sink ?: DEFAULT_SINK, "$value%")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()

    override var isMute: Boolean
        get() = command.args("get-sink-mute", sink ?: DEFAULT_SINK)
            .requireOutputText()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .endsWith("yes")
        set(value) {
            command.args("set-sink-mute", sink ?: DEFAULT_SINK, if (value) "1" else "0")
                .also { log.debug(it.prompt()) }
                .spawnCatching()
                .catchMessageFailure { message -> throw PrintMessage(message) }
                .getOrThrow()
        }

    override fun toggleVolume() {
        command.args("set-sink-mute", sink ?: DEFAULT_SINK, "toggle")
            .also { log.debug(it.prompt()) }
            .spawnCatching()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    override fun increaseVolume(value: Int) {
        volume = value
    }

    override fun decreaseVolume(value: Int) {
        volume = value
    }

    override fun listSinks(): Map<String, String> =
        command.args("list", "sinks")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToLines()
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
            .filter { """\s+Name: (.*)""".toRegex() matches it }
            .map { it.split(" ").last() }
            .mapIndexed { i, it -> "$i" to it }
            .toMap()

    companion object {
        private const val DEFAULT_SINK = "@DEFAULT_SINK@"
        private val FRONT_VOLUME_REGEX = """front-(left|right):\s+\d+\s+/\s+(\d+)%\s+/\s+\S+\s+dB""".toRegex()
    }
}
