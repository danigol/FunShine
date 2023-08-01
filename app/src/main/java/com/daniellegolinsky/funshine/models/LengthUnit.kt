package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.api.RequestDatapoints

// Used in precipitation
enum class LengthUnit(unit: String) {
    INCH(RequestDatapoints.INCH), // "inch"
    CENTIMETER(RequestDatapoints.CM) // "centimeter"
}