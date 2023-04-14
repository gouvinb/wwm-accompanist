package io.github.gouvinb.wwmaccompanist.logger.data.api

internal val Int.isNormalColor get() = this in Ansi.BLACK..Ansi.WHITE
internal val Int.isBrightColor get() = this in Ansi.BRIGHT_BLACK..Ansi.BRIGHT_WHITE
internal val Int.isColor get() = isNormalColor || isBrightColor

internal val String.firstAnsi
    get() = Ansi.regex.matchEntire(this).let { matcher ->
        if (matcher == null) null else matcher.groupValues[1].toIntOrNull()
    }
internal val String.bright
    get() = firstAnsi.let { code ->
        if (code?.isNormalColor == true) substring(0, 2) + (code + Ansi.BRIGHT_SHIFT) + substring(4) else this
    }
internal val String.bg
    get() = firstAnsi.let { code ->
        if (code?.isColor == true) substring(0, 2) + (code + Ansi.BACKGROUND_SHIFT) + substring(4) else this
    }

internal fun String.applyCodes(vararg codes: Int) = "\u001B[${Ansi.RESET}m".let { reset ->
    val tags = codes.joinToString { "\u001B[${it}m" }
    split(reset).filter { it.isNotEmpty() }.joinToString(separator = "") { tags + it + reset }
}
