package io.github.gouvinb.wwmaccompanist.util.extension

import com.kgit2.process.Command
import com.kgit2.process.Stdio

fun Command.spawnPipe() = this
    .stdout(Stdio.Pipe)
    .stderr(Stdio.Pipe)
    .spawn()

fun Command.requireOutputText() = spawnPipe().runCatching {
    waitWithOutput() ?: throw NullPointerException("Cannot read the output of the following command: `${prompt()}}`")
}

fun Command.spawnStdoutToLines() = runCatching { requireOutputText().getOrThrow().lineSequence() }

fun Command.spawnCatching() = runCatching {
    this.stdout(Stdio.Pipe)
        .stderr(Stdio.Pipe)
        .spawn()
        .let { child ->
            let {
                try {
                    child.getChildStderr()?.readText() to child.wait().code
                } catch (ex: NullPointerException) {
                    throw RuntimeException(ex.message ?: "Cannot run command `${prompt()}`", ex)
                }
            }.takeIf { (_, code) ->
                code != 0
            }?.let { (childStderr, code) ->
                throw IllegalStateException(
                    childStderr
                        ?.takeIf { it.isNotBlank() }
                        ?.let { errorOutput ->
                            """
                            |The command `${prompt()}` failed with the following message:
                            |
                            |${errorOutput.lines().joinToString("\n") { "> $it" }}
                            |
                            |Exit with code $code
                            """.trimMargin()
                        } ?: "The command `${prompt()}` exit with code $code",
                )
            }
        }
    Unit
}
