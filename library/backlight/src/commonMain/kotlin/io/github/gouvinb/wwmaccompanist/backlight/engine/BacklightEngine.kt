package io.github.gouvinb.wwmaccompanist.backlight.engine

import com.kgit2.process.Command
import io.github.gouvinb.wwmaccompanist.util.extension.check

/**
 * A sealed interface representing a backlight engine.
 * Provides functionalities to control backlight brightness and save/restore previous settings.
 *
 * @property command The command object used to interact with the backlight engine.
 * @property device The current device name of the backlight engine, null if not set.
 * @property brightness The current brightness level of the backlight engine.
 */
sealed interface BacklightEngine {
    val command: Command
    var device: String?
    var brightness: Int

    /**
     * Checks if the backlight engine is available.
     *
     * @return true if the engine is available, false otherwise.
     */
    fun isEngineAvailable() = command.check()

    /**
     * Increases the brightness level of the backlight engine by the specified value.
     *
     * @param value The value to increase the brightness level by.
     */
    fun increaseBrightness(value: Int)

    /**
     * Decreases the brightness level of the backlight engine by the specified value.
     *
     * @param value The value to decrease the brightness level by.
     */
    fun decreaseBrightness(value: Int)

    /**
     * Saves the current brightness level of the backlight engine.
     *
     * @param value The value to save as the current brightness level.
     */
    fun save(value: Int)

    /**
     * Restores the previously saved brightness level of the backlight engine.
     *
     * @param value The value to restore as the current brightness level.
     */
    fun restore(value: Int)

    /**
     * Returns a map of all available devices and their respective names for the backlight engine.
     *
     * @return A map of device names and their respective identifiers.
     */
    fun listDevices(): Map<String, String>
}
