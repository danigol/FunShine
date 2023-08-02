package com.daniellegolinsky.funshine.models

import com.daniellegolinsky.funshine.api.RequestDatapoints

// Used in precipitation
enum class LengthUnit(private val unit: String) {
    INCH(RequestDatapoints.INCH), // "inch"
    MILLIMETER(RequestDatapoints.MM); // "mm"

    override fun toString(): String {
        return unit
    }
}