package io.github.gouvinb.wwmaccompanist.logger.data.api

internal interface AnsiDisabled : Ansi {
    // Apparences

    override val bold get() = Ansi.Style.NotApplied
    override val italic get() = Ansi.Style.NotApplied
    override val underline get() = Ansi.Style.NotApplied
    override val blink get() = Ansi.Style.NotApplied
    override val reverse get() = Ansi.Style.NotApplied
    override val hidden get() = Ansi.Style.NotApplied

    // Colors

    override val red get() = Ansi.Style.NotApplied
    override val black get() = Ansi.Style.NotApplied
    override val green get() = Ansi.Style.NotApplied
    override val yellow get() = Ansi.Style.NotApplied
    override val blue get() = Ansi.Style.NotApplied
    override val purple get() = Ansi.Style.NotApplied
    override val cyan get() = Ansi.Style.NotApplied
    override val white get() = Ansi.Style.NotApplied
}
