package io.github.gouvinb.wwmaccompanist.command.bar

actual class Greeting {
    private val helloWorld: String = "Hello Native!"

    actual fun hello() = helloWorld
}
