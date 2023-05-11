package io.github.gouvinb.wwmaccompanist.util.kommand

import com.kgit2.process.Stdio

/**
 * Represents an implementation of standard input/output streams for a process.
 */
sealed class StdioImpl(val stdio: Stdio) {

    /**
     * Inherits the standard input/output streams from the parent process.
     */
    object Inherit : StdioImpl(Stdio.Inherit)

    /**
     * Discards the standard input/output streams.
     */
    object Null : StdioImpl(Stdio.Null)

    /**
     * Uses a pipe for the standard input/output streams, with an optional [stdinStr] input to be passed as input to the process.
     */
    class Pipe(
        val stdinStr: String? = null,
        val stdinByteArray: ByteArray? = null,
    ) : StdioImpl(Stdio.Pipe)
}
