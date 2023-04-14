package io.github.gouvinb.wwmaccompanist.logger.data.api

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface Ansi {

    fun <N> N.style(style: Style, predicate: (N) -> Boolean = { true }) =
        takeIf {
            predicate(this)
        }?.let {
            style.wrap(toString())
        }
            ?: toString()

    fun <N> N.wrap(vararg ansiCodes: Int) = toString().let { text ->
        if (this@Ansi is AnsiDisabled) {
            text
        } else {
            val codes = ansiCodes.filter { it != RESET }
            text.applyCodes(*codes.toIntArray())
        }
    }

    operator fun <N> N.invoke(style: Style, predicate: (N) -> Boolean = { true }) =
        style(style, predicate)

    // region apparences
    val bold: Style get() = Style.Simple(HIGH_INTENSITY)
    fun bold(text: Any) = text.wrap(HIGH_INTENSITY)

    val faint: Style get() = Style.Simple(LOW_INTENSITY)
    fun faint(text: Any) = text.wrap(LOW_INTENSITY)

    val italic: Style get() = Style.Simple(ITALIC)
    fun italic(text: String) = text.wrap(ITALIC)

    val underline: Style get() = Style.Simple(UNDERLINE)
    fun underline(text: String) = text.wrap(UNDERLINE)

    val blink: Style get() = Style.Simple(BLINK)
    fun blink(text: String) = text.wrap(BLINK)

    val reverse: Style get() = Style.Simple(REVERSE)
    fun reverse(text: String) = text.wrap(REVERSE)

    val hidden: Style get() = Style.Simple(HIDDEN)
    fun hidden(text: String) = text.wrap(HIDDEN)

    val strike: Style get() = Style.Simple(STRIKE)
    fun strike(text: String) = text.wrap(STRIKE)
    // endregion apparences

    // region colors
    val black: Style get() = Style.Simple(BLACK)
    fun black(text: String) = text.wrap(BLACK)

    val red: Style get() = Style.Simple(RED)
    fun red(text: String) = text.wrap(RED)

    val green: Style get() = Style.Simple(GREEN)
    fun green(text: String) = text.wrap(GREEN)

    val yellow: Style get() = Style.Simple(YELLOW)
    fun yellow(text: String) = text.wrap(YELLOW)

    val blue: Style get() = Style.Simple(BLUE)
    fun blue(text: String) = text.wrap(BLUE)

    val purple: Style get() = Style.Simple(PURPLE)
    fun purple(text: String) = text.wrap(PURPLE)

    val cyan: Style get() = Style.Simple(CYAN)
    fun cyan(text: String) = text.wrap(CYAN)

    val white: Style get() = Style.Simple(WHITE)
    fun white(text: String) = text.wrap(WHITE)
    // endregion colors

    sealed class Style {
        val bg: Style
            get() = when (this) {
                is Simple -> if (code.isColor) copy(code = code + BACKGROUND_SHIFT) else this
                is Composite -> if (parent is Simple && parent.code.isColor) {
                    copy(parent = parent.copy(code = parent.code + BACKGROUND_SHIFT))
                } else {
                    this
                }
                is NotApplied -> this
            }

        val bright: Style
            get() = when (this) {
                is Simple -> if (code.isNormalColor) copy(code = code + BRIGHT_SHIFT) else this
                is Composite -> if (parent is Simple && parent.code.isNormalColor) {
                    copy(parent = parent.copy(code = parent.code + BRIGHT_SHIFT))
                } else {
                    this
                }
                is NotApplied -> this
            }

        abstract fun wrap(text: String): String
        abstract operator fun plus(style: Style): Style

        object NotApplied : Style() {
            override fun wrap(text: String) = text
            override fun plus(style: Style) = this
        }

        data class Simple(val code: Int) : Style() {
            override fun wrap(text: String) = text.applyCodes(code)
            override fun plus(style: Style) = Composite(style, this)
        }

        data class Composite(val parent: Style, private val child: Style) : Style() {
            override fun wrap(text: String) = parent.wrap(child.wrap(text))
            override fun plus(style: Style) = Composite(style, this)
        }
    }

    companion object {
        internal val regex = """${"\u001B"}\[([0-9]{1,2})m""".toRegex()

        const val RESET = 0

        const val HIGH_INTENSITY = 1
        const val LOW_INTENSITY = 2

        const val BACKGROUND_SHIFT = 10
        const val BRIGHT_SHIFT = 60

        const val ITALIC = 3
        const val UNDERLINE = 4
        const val BLINK = 5
        const val REVERSE = 7
        const val HIDDEN = 8
        const val STRIKE = 9

        const val BLACK = 30
        const val RED = 31
        const val GREEN = 32
        const val YELLOW = 33
        const val BLUE = 34
        const val PURPLE = 35
        const val CYAN = 36
        const val WHITE = 37

        const val BRIGHT_BLACK = BLACK + BRIGHT_SHIFT
        const val BRIGHT_RED = RED + BRIGHT_SHIFT
        const val BRIGHT_GREEN = GREEN + BRIGHT_SHIFT
        const val BRIGHT_YELLOW = YELLOW + BRIGHT_SHIFT
        const val BRIGHT_BLUE = BLUE + BRIGHT_SHIFT
        const val BRIGHT_PURPLE = PURPLE + BRIGHT_SHIFT
        const val BRIGHT_CYAN = CYAN + BRIGHT_SHIFT
        const val BRIGHT_WHITE = WHITE + BRIGHT_SHIFT
    }
}

@ExperimentalContracts
fun <R> colored(enabled: Boolean = true, block: Ansi.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    check(true)
    return if (enabled) object : Ansi {}.block() else object : AnsiDisabled {}.block()
}

fun <R> uColored(enabled: Boolean = true, block: Ansi.() -> R): R {
    check(true)
    return if (enabled) object : Ansi {}.block() else object : AnsiDisabled {}.block()
}
