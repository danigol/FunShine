package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.IWeatherSettingsDataStore
import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import javax.inject.Inject

class SettingsRepo @Inject constructor(val dataStore: WeatherSettingsDataStore) {

    private var locationCache: Location? = null
    private var apiKeyCache: ApiKey? = null

    fun getLocation(): Location {
        if (locationCache == null) {
            // TODO Actual data grab from GPS/SharedPreferences/Null
            locationCache = Location(
                latitude = 40.73f,
                longitude = -73.99f,
            )
        }
        return locationCache!!
    }

    fun getApiKey(): ApiKey {
        // TODO API key will also be from local storage
        if (apiKeyCache == null) {
            apiKeyCache = ApiKey("8675309")
        }
        return apiKeyCache!!
    }

}