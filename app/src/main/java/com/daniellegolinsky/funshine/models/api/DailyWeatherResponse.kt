package com.daniellegolinsky.funshine.models.api

import com.daniellegolinsky.funshine.models.WeatherCode
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DailyWeatherResponse(

    @SerializedName("weathercode")
    val weatherCode: List<WeatherCode>,

    @SerializedName("temperature_2m_max")
    val maxTemp: List<Float>,

    @SerializedName("temperature_2m_min")
    val minTemp: List<Float>,

    @SerializedName("precipitation_sum")
    val precipitationSum: List<Double>,

    @SerializedName("precipitation_probability_max")
    val precipitationProbabilityMax: List<Int>
)
