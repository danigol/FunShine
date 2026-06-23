package com.daniellegolinsky.funshine.api.location

import com.daniellegolinsky.funshine.models.Location

class LocationManagerWrapper(): LocationService {
    override suspend fun getCurrentLocation(): Location {

    }
}
