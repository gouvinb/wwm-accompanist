package io.github.gouvinb.wwmaccompanist.util.error

/**
 * Always throws [NotImplementedError] stating that operation is not implemented.
 */
inline fun NOOP(): Nothing = throw NotImplementedError()

/**
 * Always throws [NotImplementedError] stating that operation is not implemented.
 *
 * @param reason a string explaining why the implementation is missing.
 */
inline fun NOOP(reason: String): Nothing = throw NotImplementedError("An operation is not implemented: $reason")
