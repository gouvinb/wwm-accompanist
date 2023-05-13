package io.github.gouvinb.wwmaccompanist.util.extension

import com.kgit2.process.Child
import com.kgit2.process.Command
import io.github.gouvinb.wwmaccompanist.env.EnvPath
import io.github.gouvinb.wwmaccompanist.util.kommand.StdioImpl
import io.ktor.utils.io.core.String
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeText

/**
 * Spawns a new process for the command, with pipes for both standard output and standard error.
 *
 * @return The [Child] object associated with the spawned process.
 */
fun Command.check() = command in EnvPath

/**
 * Spawns a new process for the command, with pipes for both standard output and standard error.
 *
 * @return The [Child] object associated with the spawned process.
 */
fun Command.spawnWithStdio(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
) = this
    .stdout(stdout.stdio)
    .stderr(stderr.stdio)
    .stdin(stdin.stdio)
    .spawn()
    .also { child ->
        if (stdin is StdioImpl.Pipe) {
            child.getChildStdin()
                ?.apply {
                    when {
                        stdin.stdinStr != null -> writeText(stdin.stdinStr)
                        stdin.stdinByteArray != null -> writeFully(stdin.stdinByteArray)
                    }
                }
        }
    }

/**
 * Requires the output of the command to be a text string, and throws an exception if the output is null.
 *
 * @return The output of the command as a text string.
 * @throws NullPointerException If the output of the command is null.
 */
fun Command.requireOutputText(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
    isTrim: Boolean = true,
) = spawnWithStdio(stdout, stderr, stdin).runCatching {
    waitWithOutput()?.run {
        if (isTrim) trim() else this
    } ?: throw NullPointerException("Cannot read the output of the following command: `${prompt()}}`")
}

/**
 * Requires the output of the command to be a text string, and throws an exception if the output is null.
 *
 * @return The output of the command as a text string.
 * @throws NullPointerException If the output of the command is null.
 */
fun Command.requireOutput(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
) = spawnWithStdio(stdout, stderr, stdin).runCatching {
    (
        getChildStdout()?.readBytes()
            ?: throw NullPointerException("Cannot read the output of the following command: `${prompt()}}`")
        )
        .apply { waitWithOutput() }
}

/**
 * Spawns a new process for the command, and returns the standard output as [String].
 *
 * @return The standard output of the command as [String].
 */
fun Command.spawnStdoutToString(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
    isTrim: Boolean = true,
) = runCatching { requireOutputText(stdout, stderr, stdin, isTrim).getOrThrow() }

/**
 * Spawns a new process for the command, and returns the standard raw output.
 *
 * @return The standard output of the command.
 */
fun Command.spawnStdout(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
) = runCatching { requireOutput(stdout, stderr, stdin).getOrThrow() }

/**
 * Spawns a new process for the command, and returns the standard output as a sequence of lines.
 *
 * @return The standard output of the command as a sequence of lines.
 */
fun Command.spawnStdoutToLines(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
) =
    runCatching { requireOutputText(stdout, stderr, stdin).getOrThrow().lineSequence() }

/**
 * Spawns a new process for the command, and catches any exceptions that occur during execution.
 *
 * @throws RuntimeException If an exception occurs during execution.
 * @throws IllegalStateException If the command exits with a non-zero exit code.
 */
fun Command.spawnCatching(
    stdout: StdioImpl = StdioImpl.Pipe(),
    stderr: StdioImpl = StdioImpl.Pipe(),
    stdin: StdioImpl = StdioImpl.Inherit,
) = runCatching {
    spawnWithStdio(stdout, stderr, stdin)
        .let { child ->
            let {
                try {
                    child.getChildStderr()?.readText() to child.run {
                        wait().code
                    }
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
