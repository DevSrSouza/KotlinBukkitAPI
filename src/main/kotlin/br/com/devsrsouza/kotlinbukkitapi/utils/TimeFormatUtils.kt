package br.com.devsrsouza.kotlinbukkitapi.utils

val Int.formater: TimeFormat get() = TimeFormat(this)

val TimeFormat.PTBR get() = format(PTBRFormat)
val TimeFormat.EN get() = format(PTBRFormat)

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

class TimeFormat(private val time: Int) {
    fun format(lang: FormatLang): String {
        val seconds = time % 60
        val minutes = time / 60 % 60
        val hours = time / 3600 % 24
        val days = time / 86400 % 7
        val weeks = time / 604800 % 4
        val months = time / 2419200 % 12
        val years = time / 29030400

        var formated = ""
        if (seconds > 0) {
            formated = "$seconds ${if (seconds > 1) lang.seconds else lang.second}"
        }
        if (minutes > 0) {
            formated = "$minutes ${if (minutes > 1) lang.minutes else lang.minute} " + formated
        }
        if (hours > 0) {
            formated = "$hours ${if (hours > 1) lang.hours else lang.hour} " + formated
        }
        if (days > 0) {
            formated = "$days ${if (days > 1) lang.days else lang.day} " + formated
        }
        if (weeks > 0) {
            formated = "$weeks ${if (weeks > 1) lang.weeks else lang.week} " + formated
        }
        if (months > 0) {
            formated = "$months ${if (months > 1) lang.months else lang.month} " + formated
        }
        if (years > 0) {
            formated = "$years ${if (years > 1) lang.years else lang.year} " + formated
        }
        return formated.trim()
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