package br.com.devsrsouza.kotlinbukkitapi.utility.types

import kotlinx.serialization.Serializable

public const val DEFAULT_SPACER: Char = ' '
public const val DEFAULT_FORMAT_STYLE: String = "%YEAR %MONTH %WEEK %DAY %HOUR %MIN %SEC"

public val Long.formatter: TimeFormat get() = TimeFormat(this)
public val Int.formatter: TimeFormat get() = TimeFormat(this.toLong())

public val TimeFormat.PTBR: String get() = format(PTBRFormat)
public val TimeFormat.EN: String get() = format(ENFormat)

@Serializable
public data class FormatLang(
    val second: String,
    val seconds: String,
    val minute: String,
    val minutes: String,
    val hour: String,
    val hours: String,
    val day: String,
    val days: String,
    val week: String,
    val weeks: String,
    val month: String,
    val months: String,
    val year: String,
    val years: String,
)

/**
 * @param time in second
 */
@JvmInline
public value class TimeFormat(private val time: Long) {
    public fun format(
        lang: FormatLang,
        formatStyle: String = DEFAULT_FORMAT_STYLE,
        formatSpacer: Char = DEFAULT_SPACER,
    ): String {
        val seconds = time % 60
        val minutes = time / 60 % 60
        val hours = time / 3600 % 24
        val days = time / 86400 % 7
        val weeks = time / 604800 % 4
        val months = time / 2419200 % 12
        val years = time / 29030400

        var formated = formatStyle.replace("%SEC", if (seconds > 0) "$seconds ${if (seconds > 1) lang.seconds else lang.second}" else "", true)

        formated = formated.replace("%MIN", if (minutes > 0) "$minutes ${if (minutes > 1) lang.minutes else lang.minute}" else "", true)

        formated = formated.replace("%HOUR", if (hours > 0) "$hours ${if (hours > 1) lang.hours else lang.hour}" else "", true)

        formated = formated.replace("%DAY", if (days > 0) "$days ${if (days > 1) lang.days else lang.day}" else "", true)

        formated = formated.replace("%WEEK", if (weeks > 0) "$weeks ${if (weeks > 1) lang.weeks else lang.week}" else "", true)

        formated = formated.replace("%MONTH", if (months > 0) "$months ${if (months > 1) lang.months else lang.month}" else "", true)

        formated = formated.replace("%YEAR", if (years > 0) "$years ${if (years > 1) lang.years else lang.year}" else "", true)

        formated = formated.replace("$formatSpacer$formatSpacer", "$formatSpacer")

        return formated.trim { it == formatSpacer }
    }
}

public val PTBRFormat: FormatLang = FormatLang(
    second = "segundo",
    seconds = "segundos",

    minute = "minuto",
    minutes = "minutos",

    hour = "hora",
    hours = "horas",

    day = "dia",
    days = "dias",

    week = "semana",
    weeks = "semanas",

    month = "mes",
    months = "meses",

    year = "ano",
    years = "anos",
)

public val ENFormat: FormatLang = FormatLang(
    second = "second",
    seconds = "seconds",

    minute = "minute",
    minutes = "minutes",

    hour = "hour",
    hours = "hours",

    day = "day",
    days = "days",

    week = "week",
    weeks = "weeks",

    month = "month",
    months = "months",

    year = "year",
    years = "years",
)
