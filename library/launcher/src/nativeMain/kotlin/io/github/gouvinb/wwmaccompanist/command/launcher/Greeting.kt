package io.github.gouvinb.wwmaccompanist.command.launcher

actual class Greeting {
    private val helloWorld: String = "Hello Native!"

    actual fun hello() = helloWorld
}
