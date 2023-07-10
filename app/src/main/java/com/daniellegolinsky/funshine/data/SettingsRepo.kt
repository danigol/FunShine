package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.singleOrNull
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    private val dataStore: WeatherSettingsDataStore,
) {
    suspend fun setApiKey(key: String) {
        dataStore.setApiKey(ApiKey(key))
    }
    suspend fun setLocation(lat: Float, long: Float) {
        dataStore.setLocation(Location(lat, long))
    }

    suspend fun getApiKey(): ApiKey {
        return dataStore.getApiKey()
    }

    suspend fun getLocation(): Location {
        // TODO Get GPS location optionally?
        return dataStore.getLocation()
    }
}