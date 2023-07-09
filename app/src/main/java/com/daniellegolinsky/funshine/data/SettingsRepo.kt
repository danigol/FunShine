package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    val dataStore: WeatherSettingsDataStore,
) {

    private var locationCache: Location? = null
    private var apiKeyCache: ApiKey? = null

    suspend fun setApiKey(key: String) {
        apiKeyCache = ApiKey(key)
        dataStore.setApiKey(apiKeyCache!!)
    }
    suspend fun setLocation(lat: Float, long: Float) {
        locationCache = Location(lat, long)
        dataStore.setLocation(locationCache!!)
    }

    suspend fun getLocation(): Location {
        if (locationCache == null) {
            // TODO Actual data grab from GPS
            locationCache = dataStore.getLocation()
//            if (locationCache == null) {
//                locationCache = Location(
//                    latitude = 40.73f,
//                    longitude = -73.99f,
//                )
//            }
        }
        return locationCache!!
    }

    suspend fun getApiKey(): ApiKey {
        // TODO API key will also be from local storage
        if (apiKeyCache == null) {
            apiKeyCache = dataStore.getApiKey()
        }
        return apiKeyCache!!
    }

}