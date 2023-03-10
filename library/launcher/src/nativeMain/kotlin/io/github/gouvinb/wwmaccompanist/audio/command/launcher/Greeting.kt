package io.github.gouvinb.wwmaccompanist.audio.command.launcher

actual class Greeting {
    private val helloWorld: String = "Hello Native!"

    actual fun hello() = helloWorld
}
