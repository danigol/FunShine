package com.daniellegolinsky.funshine.datastore

import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location

interface IWeatherSettingsDataStore {
    suspend fun getLocation(): Location
    suspend fun setLocation(location: Location)
    suspend fun getHasSeenLocationWarning(): Boolean
    suspend fun setHasSeenLocationWarning(hasSeen: Boolean)
}