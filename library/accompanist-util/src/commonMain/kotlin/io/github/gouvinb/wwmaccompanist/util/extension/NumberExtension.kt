package io.github.gouvinb.wwmaccompanist.util.extension

fun <T : Number> T.asString(padStart: Int = 0, padEnd: Int = 0) = toString()
    .let { if (padStart > 0) it.padStart(padStart, '0') else it }
    .let { if (padEnd > 0) it.padEnd(padEnd, '0') else it }
