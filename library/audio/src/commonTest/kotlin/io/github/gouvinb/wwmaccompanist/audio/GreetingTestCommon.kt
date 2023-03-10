package io.github.gouvinb.wwmaccompanist.audio

import io.github.gouvinb.wwmaccompanist.audio.command.AudioCommand
import kotlin.test.Test
import kotlin.test.assertTrue

open class GreetingTestCommon {

    internal val greeting = AudioCommand()

    @Test
    open fun hello() {
        assertTrue(greeting.hello() matches """Hello [A-Za-z]+!""".toRegex())
    }
}