package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.api.RequestDatapoints

enum class TemperatureUnit(unit: String) {
    FAHRENHEIT(RequestDatapoints.F), // "fahrenheit"
    CELSIUS(RequestDatapoints.C) // "celsius"
}
