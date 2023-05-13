package io.github.gouvinb.wwmaccompanist.screenshot.target

import com.github.ajalt.clikt.core.PrintMessage
import com.kgit2.process.Command
import io.github.gouvinb.wwmaccompanist.env.XdgBaseDirectoryVariables
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.spawnCatching
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdout
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToString
import io.github.gouvinb.wwmaccompanist.util.extension.toTimestamp
import io.github.gouvinb.wwmaccompanist.util.kommand.StdioImpl
import kotlinx.datetime.Clock
import okio.FileSystem
import okio.Path.Companion.toPath

/**
 * Abstract class representing a screenshot target.
 * @param T a generic type representing the target data.
 */
abstract class ScreenshotTarget<T : ScreenshotTarget.TargetData> {
    /**
     * String representing the geometry of the target.
     */
    abstract val geometry: String

    /**
     * String representing what the target is.
     */
    abstract val what: String

    /**
     * The target data.
     */
    lateinit var data: T

    private var outputFilePath = FILE_PATH

    /**
     * Initializes the target data.
     * @return the initialized ScreenshotTarget.
     */
    abstract fun initData(): ScreenshotTarget<T>

    /**
     * Override output file path.
     *
     * @param filePath the file path..
     */
    fun setOutputFilePath(filePath: String): ScreenshotTarget<T> {
        outputFilePath = filePath
        return this
    }

    /**
     * Takes a screenshot of the target.
     * @param action the action to perform on the target.
     * @throws IllegalStateException if the target has changed and the action is COPY.
     */
    fun takeScreenshot(action: TargetActionType) {
        require(this::data.isInitialized) { "The `$what` target must be initialised." }
        when (action) {
            TargetActionType.COPY -> {
                val stdinByteArray = grimCommand()
                    .run {
                        when {
                            geometry.isNotBlank() -> args("-g", data.target, REDIRECT_OUTPUT_PATH)
                            data.target.isNotBlank() -> args("-o", data.target, REDIRECT_OUTPUT_PATH)
                            else -> args(REDIRECT_OUTPUT_PATH)
                        }
                    }
                    .also { log.debug(it.prompt()) }
                    .spawnStdout()
                    .catchMessageFailure { message -> throw PrintMessage(message) }
                    .getOrThrow()

                wlCopyCommand()
                    .also { log.debug(it.prompt()) }
                    .spawnCatching(
                        stderr = StdioImpl.Null,
                        stdin = StdioImpl.Pipe(stdinByteArray = stdinByteArray),
                    )
                    .catchMessageFailure { message -> throw PrintMessage(message) }
                    .getOrThrow()
            }

            TargetActionType.SAVE, TargetActionType.SWAPPY -> {
                grimCommand()
                    .run {
                        when {
                            geometry.isNotBlank() -> args("-g", data.target, TMP_FILE_PATH)
                            data.target.isNotBlank() -> args("-o", data.target, TMP_FILE_PATH)
                            else -> args(TMP_FILE_PATH)
                        }
                    }
                    .also { log.debug(it.prompt()) }
                    .spawnCatching()
                    .getOrThrow()
                if (action == TargetActionType.SWAPPY) {
                    swappyCommand()
                }
                FileSystem.SYSTEM.copy(TMP_FILE_PATH.toPath(), FILE_PATH.toPath())
                FileSystem.SYSTEM.delete(TMP_FILE_PATH.toPath(), false)
            }
        }
    }

    /**
     * Returns a Command object representing the grim command.
     * @return a Command object representing the grim command.
     */
    fun grimCommand() = Command("grim")

    /**
     * Returns a Command object representing the jq command.
     * @return a Command object representing the jq command.
     */
    fun jqCommand() = Command("jq")

    /**
     * Returns a Command object representing the slurp command.
     * @return a Command object representing the slurp command.
     */
    fun slurpCommand() = Command("slurp")

    /**
     * Returns a Command object representing the swappy command.
     * @return a Command object representing the swappy command.
     * @throws PrintMessage if an error occurs while executing the command.
     */
    fun swappyCommand() = Command("swappy")
        .args("-f", TMP_FILE_PATH, "-o", TMP_FILE_PATH)
        .also { log.debug(it.prompt()) }
        .spawnStdoutToString(stderr = StdioImpl.Null)
        .catchMessageFailure { message -> throw PrintMessage(message) }
        .getOrThrow()

    /**
     * Returns a Command object representing the swaymsg command.
     * @return a Command object representing the swaymsg command.
     */
    fun swaymsgCommand() = Command("swaymsg")

    /**
     * Gets the outputs from the Sway window manager.
     * @return a String representing the outputs.
     * @throws PrintMessage if an error occurs while executing the command.
     */
    fun swaymsgGetOutputs() = swaymsgCommand()
        .args("-t", "get_outputs")
        .also { log.debug(it.prompt()) }
        .spawnStdoutToString()
        .catchMessageFailure { message -> throw PrintMessage(message) }
        .getOrThrow()

    /**
     * Gets the tree of windows from the Sway window manager.
     * @return a String representing the tree of windows.
     * @throws PrintMessage if an error occurs while executing the command.
     */
    fun swaymsgGetTree() = swaymsgCommand()
        .args("-t", "get_tree")
        .also { log.debug(it.prompt()) }
        .spawnStdoutToString()
        .catchMessageFailure { message -> throw PrintMessage(message) }
        .getOrThrow()

    /**
     * Returns a Command object representing the wl-copy command.
     * @return a Command object representing the wl-copy command.
     */
    fun wlCopyCommand() = Command("wl-copy")
        .args("--type", "image/png")

    /**
     * Abstract class representing the target data.
     * @property target the target.
     * @property what what the target is.
     */
    abstract class TargetData(open val target: String, open val what: String)

    companion object {
        /**
         * Stdout output path.
         */
        const val REDIRECT_OUTPUT_PATH = "-"

        /**
         * The path where the output file will be saved.
         */
        val FILE_PATH = "${XdgBaseDirectoryVariables().xdgScreenshotsDir}/${Clock.System.now().toTimestamp()}.png"

        /**
         * The path where the output file will temporarily be saved.
         */
        val TMP_FILE_PATH = "${XdgBaseDirectoryVariables().tmpDir}/${Clock.System.now().toTimestamp()}.png"
    }
}
