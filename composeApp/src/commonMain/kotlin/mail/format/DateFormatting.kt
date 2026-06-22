package mail.format

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import mail.data.MockMailSource

private val weekdayAbbrev = mapOf(
    DayOfWeek.MONDAY to "Mon",
    DayOfWeek.TUESDAY to "Tue",
    DayOfWeek.WEDNESDAY to "Wed",
    DayOfWeek.THURSDAY to "Thu",
    DayOfWeek.FRIDAY to "Fri",
    DayOfWeek.SATURDAY to "Sat",
    DayOfWeek.SUNDAY to "Sun",
)

private val monthAbbrev = mapOf(
    Month.JANUARY to "Jan", Month.FEBRUARY to "Feb", Month.MARCH to "Mar",
    Month.APRIL to "Apr", Month.MAY to "May", Month.JUNE to "Jun",
    Month.JULY to "Jul", Month.AUGUST to "Aug", Month.SEPTEMBER to "Sep",
    Month.OCTOBER to "Oct", Month.NOVEMBER to "Nov", Month.DECEMBER to "Dec",
)

private fun pad2(n: Int): String = if (n < 10) "0$n" else n.toString()

fun formatHm(dt: LocalDateTime): String = "${pad2(dt.hour)}:${pad2(dt.minute)}"

private fun wholeDaysBetween(earlier: LocalDateTime, later: LocalDateTime): Int {
    val minutes = (later.toInstant(TimeZone.UTC) - earlier.toInstant(TimeZone.UTC)).inWholeMinutes
    return (minutes / (24 * 60)).toInt()
}

/**
 * Mirrors the original list-row timestamp: clock time for today, "Yesterday",
 * or a weekday abbreviation further back — relative to the mock data's fixed "now".
 */
fun formatMessageTime(dt: LocalDateTime, now: LocalDateTime = MockMailSource.fixedNow): String {
    val diffDays = wholeDaysBetween(dt, now)
    return when {
        diffDays >= 2 -> weekdayAbbrev.getValue(dt.dayOfWeek)
        diffDays >= 1 -> "Yesterday"
        else -> formatHm(dt)
    }
}

/** e.g. "Sun, Jun 21 — 14:02" */
fun formatReadingDate(dt: LocalDateTime): String =
    "${weekdayAbbrev.getValue(dt.dayOfWeek)}, ${monthAbbrev.getValue(dt.month)} ${dt.dayOfMonth} — ${formatHm(dt)}"
