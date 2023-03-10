package io.github.gouvinb.wwmaccompanist.util.extension

fun <T> Result<T>.catchMessageFailure(action: (message: String) -> Unit) =
    onFailure {
        it.message
            ?.let { message -> action(message) }
            ?: throw it
    }
