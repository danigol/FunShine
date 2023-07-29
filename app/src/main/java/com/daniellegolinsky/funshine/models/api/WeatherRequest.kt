package com.daniellegolinsky.funshine.models.api

import com.daniellegolinsky.funshine.models.Location

data class WeatherRequest(
    val location: Location,
)