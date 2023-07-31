package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.Location
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    private val dataStore: WeatherSettingsDataStore,
) {
    suspend fun setLocation(lat: Float, long: Float) {
        dataStore.setLocation(Location(lat, long))
    }

    suspend fun getLocation(): Location {
        return dataStore.getLocation()
    }

    suspend fun setHasSeenLocationWarning(hasSeen: Boolean) {
        dataStore.setHasSeenLocationWarning(hasSeen)
    }
    suspend fun getHasSeenLocationWarning(): Boolean {
        return dataStore.getHasSeenLocationWarning()
    }
}