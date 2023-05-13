package io.github.gouvinb.wwmaccompanist.screenshot.target.impl

import com.github.ajalt.clikt.core.PrintMessage
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.screenshot.target.ScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.ActiveScreenshotTarget.ActiveTargetData
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToString
import io.github.gouvinb.wwmaccompanist.util.kommand.StdioImpl

/**
 * A `ScreenshotTarget` that represents the currently active window.
 *
 * @property focused The JSON output of
 *                   `swaymsg -t get_tree | jq recurse(.nodes[]?, .floating_nodes[]?) | select(.focused)`.
 * @property geometry The geometry of the currently focused window as a string, in the format `"x,y widthxheight"`.
 * @property appId The application ID of the currently focused window as a string.
 */
class ActiveScreenshotTarget : ScreenshotTarget<ActiveTargetData>() {

    /**
     * Lazily initialized property that contains the JSON output of
     * `swaymsg -t get_tree | jq recurse(.nodes[]?, .floating_nodes[]?) | select(.focused)`.
     */
    private val focused by lazy {
        jqCommand()
            .args("-r", "recurse(.nodes[]?, .floating_nodes[]?) | select(.focused)")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToString(stdin = StdioImpl.Pipe(stdinStr = swaymsgGetTree()))
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    /**
     * Lazily initialized property that contains the geometry of the currently focused window as a string, in the format
     * `"x,y widthxheight"`.
     */
    override val geometry by lazy {
        jqCommand()
            .args("-r", ".rect | \"\\(.x),\\(.y) \\(.width)x\\(.height)\"")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToString(stdin = StdioImpl.Pipe(stdinStr = focused))
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    /**
     * Lazily initialized property that contains the application ID of the currently focused window as a string.
     */
    private val appId by lazy {
        jqCommand()
            .args("-r", ".app_id")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToString(stdin = StdioImpl.Pipe(stdinStr = focused))
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    /**
     * The description of the `ActiveScreenshotTarget` instance as a string, in the format "`appId` window's".
     */
    override val what by lazy { "$appId window's" }

    /**
     * Initializes the `data` property of this `ActiveScreenshotTarget`.
     *
     * @return This `ActiveScreenshotTarget`.
     */
    override fun initData(): ActiveScreenshotTarget {
        data = ActiveTargetData(geometry, what)
        return this
    }

    /**
     * Data class representing the target data for an `ActiveScreenshotTarget`.
     *
     * @property target The target string for the `ActiveScreenshotTarget`.
     * @property what The description of the `ActiveScreenshotTarget`.
     */
    data class ActiveTargetData(override val target: String, override val what: String) : TargetData(target, what)
}
