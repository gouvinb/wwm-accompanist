package io.github.gouvinb.wwmaccompanist.util.extension

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toTimestamp(timeZone: TimeZone = TimeZone.currentSystemDefault()) = toLocalDateTime(timeZone)
    .run {
        buildString {
            append(year.asString(4))
            append("-")
            append(monthNumber.asString(2))
            append("-")
            append(dayOfMonth.asString(2))
            append("T")
            append(hour.asString(2))
            append(":")
            append(minute.asString(2))
            append(":")
            append(second.asString(2))
        }
    }
