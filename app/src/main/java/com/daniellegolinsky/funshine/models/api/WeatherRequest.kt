package com.daniellegolinsky.funshine.models.api

import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import kotlinx.serialization.Serializable

@Serializable
data class WeatherRequest(
    val location: Location,
    val tempUnit: TemperatureUnit,
    val speedUnit: SpeedUnit,
    val lengthUnit: LengthUnit,
) {
    override fun equals(other: Any?): Boolean {
        return if (other is WeatherRequest) {
            location == other.location
                    && tempUnit == other.tempUnit
                    && speedUnit == other.speedUnit
                    && lengthUnit == other.lengthUnit
        } else {
            false
        }
    }
}
