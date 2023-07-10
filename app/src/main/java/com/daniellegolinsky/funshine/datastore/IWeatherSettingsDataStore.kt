package com.daniellegolinsky.funshine.datastore

import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location

interface IWeatherSettingsDataStore {
    suspend fun getApiKey(): ApiKey
    suspend fun getLocation(): Location

    suspend fun setApiKey(apiKey: ApiKey)
    suspend fun setLocation(location: Location)
}