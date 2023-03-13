package io.github.gouvinb.wwmaccompanist.util.extension

import com.kgit2.process.Child
import com.kgit2.process.Command
import com.kgit2.process.Stdio

/**
 * Spawns a new process for the command, with pipes for both standard output and standard error.
 *
 * @return The [Child] object associated with the spawned process.
 */
fun Command.spawnPipe() = this
    .stdout(Stdio.Pipe)
    .stderr(Stdio.Pipe)
    .spawn()

/**
 * Requires the output of the command to be a text string, and throws an exception if the output is null.
 *
 * @return The output of the command as a text string.
 * @throws NullPointerException If the output of the command is null.
 */
fun Command.requireOutputText() = spawnPipe().runCatching {
    waitWithOutput() ?: throw NullPointerException("Cannot read the output of the following command: `${prompt()}}`")
}

/**
 * Spawns a new process for the command, and returns the standard output as a sequence of lines.
 *
 * @return The standard output of the command as a sequence of lines.
 */
fun Command.spawnStdoutToLines() = runCatching { requireOutputText().getOrThrow().lineSequence() }

/**
 * Spawns a new process for the command, and catches any exceptions that occur during execution.
 *
 * @throws RuntimeException If an exception occurs during execution.
 * @throws IllegalStateException If the command exits with a non-zero exit code.
 */
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
