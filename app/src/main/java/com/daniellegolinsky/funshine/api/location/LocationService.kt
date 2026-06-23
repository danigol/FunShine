package com.daniellegolinsky.funshine.api.location

import com.daniellegolinsky.funshine.models.Location

interface LocationService {
    fun getCurrentLocation(): Location
}
