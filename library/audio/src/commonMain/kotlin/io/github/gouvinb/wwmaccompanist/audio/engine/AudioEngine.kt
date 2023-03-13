package io.github.gouvinb.wwmaccompanist.audio.engine

import com.kgit2.process.Command

/**
 * A sealed interface defining an audio engine.
 *
 * This interface defines the basic functionality for audio management.
 *
 * @property command The command object associated with the audio engine.
 * @property sink The sink to use for audio output.
 * @property volume The volume level of the audio.
 * @property isMute A boolean indicating if the audio is muted.
 */
sealed interface AudioEngine {
    val command: Command
    var sink: String?

    var volume: Int
    var isMute: Boolean

    /**
     * Toggles the volume of the audio.
     */
    fun toggleVolume()

    /**
     * Increases the volume of the audio by the given value.
     *
     * @param value The value to increase the volume by.
     */
    fun increaseVolume(value: Int)

    /**
     * Decreases the volume of the audio by the given value.
     *
     * @param value The value to decrease the volume by.
     */
    fun decreaseVolume(value: Int)

    /**
     * Lists the available sinks for audio output.
     *
     * @return A map of strings representing the available sinks and their descriptions.
     */
    fun listSinks(): Map<String, String>
}
