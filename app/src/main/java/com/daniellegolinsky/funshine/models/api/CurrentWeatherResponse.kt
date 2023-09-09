package com.daniellegolinsky.funshine.models.api

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerializedName("temperature")
    val temperature: Float,

    @SerializedName("weathercode")
    val weatherCodeInt: Int,

    @SerializedName("is_day")
    val isDay: Int,

    @SerializedName("windspeed")
    val windSpeed: Float,
)
