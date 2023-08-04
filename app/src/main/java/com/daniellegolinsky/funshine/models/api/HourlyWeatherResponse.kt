package com.daniellegolinsky.funshine.models.api

import com.google.gson.annotations.SerializedName

data class HourlyWeatherResponse(
    @SerializedName("time")
    val timeList: List<String>,

    @SerializedName("temperature_2m")
    val temperatureList: List<Float>,

    @SerializedName("relativehumidity_2m")
    val humidityList: List<Int>,
)
