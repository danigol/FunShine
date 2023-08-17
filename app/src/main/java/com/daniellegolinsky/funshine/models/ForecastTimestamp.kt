package com.daniellegolinsky.funshine.models

import kotlinx.serialization.Serializable

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
    override fun equals(other: Any?): Boolean {
        return if (other is ForecastTimestamp) {
            this.hour == other.hour && this.day == other.day && this.year == other.year
        } else {
            false
        }
    }
}
