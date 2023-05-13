package io.github.gouvinb.wwmaccompanist.screenshot.target.impl

import io.github.gouvinb.wwmaccompanist.screenshot.target.ScreenshotTarget
import io.github.gouvinb.wwmaccompanist.screenshot.target.impl.ScreenScreenshotTarget.ScreenTargetData

/**
 * A [ScreenshotTarget] that represents a screen target.
 */
class ScreenScreenshotTarget : ScreenshotTarget<ScreenTargetData>() {
    /**
     * The geometry of the screen target, which is an empty string for this type of target.
     */
    override val geometry = ""

    /**
     * A string that describes what this target represents, which is "Screen" for this type of target.
     */
    override val what = "Screen"

    /**
     * Initializes the data of this target.
     *
     * @return This instance of [ScreenScreenshotTarget].
     */
    override fun initData(): ScreenScreenshotTarget {
        data = ScreenTargetData(what)
        return this
    }

    /**
     * The data of the screen target, which contains information about the target.
     *
     * @param what A string that describes what this target represents.
     * @param target The target of the screenshot, which is an empty string for this type of target.
     */
    data class ScreenTargetData(
        override val what: String,
        override val target: String = "",
    ) : TargetData(target, what)
}
