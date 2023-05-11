package io.github.gouvinb.wwmaccompanist.application

import com.github.ajalt.clikt.output.CliktHelpFormatter
import io.github.gouvinb.wwmaccompanist.env.WinSize

object WwmAccompanistHelpFormatter : CliktHelpFormatter(
    indent = "\t",
    maxWidth = WinSize().wsCol.toInt().takeIf { it > 0 } ?: Int.MAX_VALUE,
    colSpacing = 4,
    showDefaultValues = true,
    showRequiredTag = true,
)
