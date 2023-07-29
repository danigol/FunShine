package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.models.api.CurrentWeatherResponse
import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @SerializedName("current_weather")
    val currentWeather: CurrentWeatherResponse,

    )
