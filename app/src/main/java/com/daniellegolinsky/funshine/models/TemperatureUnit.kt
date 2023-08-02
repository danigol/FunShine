package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.api.RequestDatapoints

enum class TemperatureUnit(private val unit: String) {
    FAHRENHEIT(RequestDatapoints.F), // "fahrenheit"
    CELSIUS(RequestDatapoints.C); // "celsius"

    override fun toString(): String {
        return unit
    }
}
