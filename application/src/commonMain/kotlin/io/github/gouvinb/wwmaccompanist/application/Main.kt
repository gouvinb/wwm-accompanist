package io.github.gouvinb.wwmaccompanist.application

import io.github.gouvinb.wwmaccompanist.command.audio.Greeting

fun main() {
    println(
        """
            |Application:
            |	${Greeting().hello()}
        """.trimMargin(),
    )
}
