package io.github.gouvinb.wwmaccompanist.audio.engine

import com.kgit2.process.Command

sealed interface AudioEngine {
    val command: Command
    var sink: String?

    var volume: Int
    var isMute: Boolean

    fun toggleVolume()
    fun increaseVolume(value: Int)
    fun decreaseVolume(value: Int)

    fun listSinks(): Map<String, String>
}
