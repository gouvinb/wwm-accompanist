package io.github.gouvinb.wwmaccompanist.backlight.engine

import com.kgit2.process.Command

sealed interface BacklightEngine {
    val command: Command

    var device: String?
    var brightness: Int

    fun increaseBrightness(value: Int)

    fun decreaseBrightness(value: Int)

    fun save(value: Int)

    fun restore(value: Int)

    fun listDevices(): Map<String, String>
}
