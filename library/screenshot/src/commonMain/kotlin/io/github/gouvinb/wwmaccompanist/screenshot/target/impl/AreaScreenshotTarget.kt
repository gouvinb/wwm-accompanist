package io.github.gouvinb.wwmaccompanist.screenshot.target.impl

import com.github.ajalt.clikt.core.PrintMessage
import io.github.gouvinb.wwmaccompanist.logger.presentation.log
import io.github.gouvinb.wwmaccompanist.screenshot.target.ScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.AreaScreenshotTarget.AreaTargetData
import io.github.gouvinb.wwmaccompanist.util.extension.catchMessageFailure
import io.github.gouvinb.wwmaccompanist.util.extension.spawnStdoutToString
import io.github.gouvinb.wwmaccompanist.util.kommand.StdioImpl

/**
 * Screenshot target for a specified area of the screen.
 */
class AreaScreenshotTarget : ScreenshotTarget<AreaTargetData>() {

    /**
     * The geometry of the area to capture.
     */
    /**
     * The geometry of the area to capture.
     */
    override val geometry: String by lazy {
        slurpCommand()
            .args("-d")
            .also { log.debug(it.prompt()) }
            .spawnStdoutToString(stdin = StdioImpl.Null)
            .catchMessageFailure { message -> throw PrintMessage(message) }
            .getOrThrow()
    }

    /**
     * A string representation of what this screenshot target captures.
     */
    override val what: String
        get() = "Area"

    /**
     * Initialize the target data for this screenshot target.
     *
     * @return This screenshot target instance.
     */
    override fun initData(): AreaScreenshotTarget {
        data = AreaTargetData(geometry, what)
        return this
    }

    /**
     * Data class representing the target data for an area screenshot target.
     *
     * @param target The geometry of the area to capture.
     * @param what A string representation of what this screenshot target captures.
     */
    data class AreaTargetData(override val target: String, override val what: String) : TargetData(target, what)
}
