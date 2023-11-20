package com.daniellegolinsky.funshine.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Forecasts can only be updated on a 1 hour interval from open meteo
 * So we check whether or not the forecast is recent, and, if it's not, we toss it.
 * Otherwise, cache away!
 */
@Serializable
data class ForecastTimestamp(
    val year: Int,
    val day: Int,
    val hour: Int,
) {
    companion object{
        const val HOURS_IN_DAY: Int = 24
        const val HOURS_IN_YEAR: Int = HOURS_IN_DAY * 365
        fun getCurrentTimestamp(): ForecastTimestamp {
            val currentHour = LocalDateTime.now().hour
            val currentDay = LocalDateTime.now().dayOfMonth
            val currentYear = LocalDateTime.now().year
            return ForecastTimestamp(year = currentYear, day = currentDay, hour = currentHour)
        }
    }
    override fun equals(other: Any?): Boolean {
        return if (other is ForecastTimestamp) {
            this.hour == other.hour && this.day == other.day && this.year == other.year
        } else {
            false
        }
    }

    fun tomorrow(): ForecastTimestamp {
        val daysInYear = if (year % 4 == 0) 366 else 365
        var newDay: Int
        var newYear: Int
        if (this.day + 1 > daysInYear) {
            newDay = 0
            newYear = this.year + 1
        } else {
            newDay = this.day + 1
            newYear = this.year
        }
        return ForecastTimestamp(
            year = newYear,
            day = newDay,
            hour = this.hour
        )
    }
}

fun ForecastTimestamp.hoursBetween(laterTimestamp: ForecastTimestamp): Int {
    val yearsBetween = laterTimestamp.year - this.year
    val daysBetween = laterTimestamp.day - this.day
    val hoursBetween = laterTimestamp.hour - this.hour

    return (yearsBetween * ForecastTimestamp.HOURS_IN_YEAR) + (daysBetween * ForecastTimestamp.HOURS_IN_DAY) + hoursBetween
}
