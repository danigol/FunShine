package com.daniellegolinsky.funshine.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Float,
    val longitude: Float,
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Location) {
            latitude == other.latitude && longitude == other.longitude
        } else {
            false
        }
    }
}
