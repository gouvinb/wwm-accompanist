package io.github.gouvinb.wwmaccompanist.util.extension

/**
 * Executes the given [action] if the [Result] is a failure with a message. If the [Result] is a failure without a
 * message, the exception is re-thrown.
 *
 * @param action The action to execute with the failure message.
 * @throws Throwable If the [Result] is a failure without a message.
 */
fun <T> Result<T>.catchMessageFailure(action: (message: String) -> Unit) =
    onFailure {
        it.message
            ?.let { message -> action(message) }
            ?: throw it
    }
