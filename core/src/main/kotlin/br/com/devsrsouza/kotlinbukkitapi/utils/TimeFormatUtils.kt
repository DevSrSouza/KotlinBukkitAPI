package br.com.devsrsouza.kotlinbukkitapi.utils

const val DEFAULT_SPACER = ' '
const val DEFAULT_FORMAT_STYLE = "%YEAR %MONTH %WEEK %DAY %HOUR %MIN %SEC"

val Int.formater: TimeFormat get() = TimeFormat(this)

val TimeFormat.PTBR get() = format(PTBRFormat)
val TimeFormat.EN get() = format(ENFormat)

interface FormatLang {
    val second: String
    val seconds: String
    val minute: String
    val minutes: String
    val hour: String
    val hours: String
    val day: String
    val days: String
    val week: String
    val weeks: String
    val month: String
    val months: String
    val year: String
    val years: String
}

inline class TimeFormat(private val time: Int) {
    fun format(lang: FormatLang,
               formatStyle: String = DEFAULT_FORMAT_STYLE,
               formatSpacer: Char = DEFAULT_SPACER): String {

        val seconds = time % 60
        val minutes = time / 60 % 60
        val hours = time / 3600 % 24
        val days = time / 86400 % 7
        val weeks = time / 604800 % 4
        val months = time / 2419200 % 12
        val years = time / 29030400

        var formated = formatStyle.replace(if (seconds > 0) "$seconds ${if (seconds > 1) lang.seconds else lang.second}" else "", "%SEC", true)

        formated = formated.replace(if (minutes > 0) "$minutes ${if (minutes > 1) lang.minutes else lang.minute}" else "", "%MIN", true)

        formated = formated.replace(if (hours > 0) "$hours ${if (hours > 1) lang.hours else lang.hour}" else "", "%HOUR", true)

        formated = formated.replace(if (days > 0) "$days ${if (days > 1) lang.days else lang.day}" else "", "%DAY", true)

        formated = formated.replace(if (weeks > 0) "$weeks ${if (weeks > 1) lang.weeks else lang.week}" else "", "%WEEK", true)

        formated = formated.replace(if (months > 0) "$months ${if (months > 1) lang.months else lang.month}" else "", "%MONTH", true)

        formated = formated.replace(if (years > 0) "$years ${if (years > 1) lang.years else lang.year}" else "", "%YEAR", true)

        return formated.trim { it == formatSpacer }
    }
}

object PTBRFormat : FormatLang {
    override val second: String get() = "segundo"
    override val seconds: String get() = "segundos"

    override val minute: String get() = "minuto"
    override val minutes: String get() = "minutos"

    override val hour: String get() = "hora"
    override val hours: String get() = "horas"

    override val day: String get() = "dia"
    override val days: String get() = "dias"

    override val week: String get() = "semana"
    override val weeks: String get() = "semanas"

    override val month: String get() = "mes"
    override val months: String get() = "meses"

    override val year: String get() = "ano"
    override val years: String get() = "anos"
}

object ENFormat : FormatLang {
    override val second: String get() = "second"
    override val seconds: String get() = "seconds"

    override val minute: String get() = "minute"
    override val minutes: String get() = "minutes"

    override val hour: String get() = "hour"
    override val hours: String get() = "hours"

    override val day: String get() = "day"
    override val days: String get() = "days"

    override val week: String get() = "week"
    override val weeks: String get() = "weeks"

    override val month: String get() = "month"
    override val months: String get() = "months"

    override val year: String get() = "year"
    override val years: String get() = "years"
}