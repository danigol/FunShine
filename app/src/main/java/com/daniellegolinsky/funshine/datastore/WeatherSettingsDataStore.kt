package com.daniellegolinsky.funshine.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class WeatherSettingsDataStore @Inject constructor(
    @Named(ApplicationModule.SETTINGS_DATASTORE) val dataStore: DataStore<Preferences>
) : IWeatherSettingsDataStore {

    private object StoreKeys {
        val API_KEY = stringPreferencesKey("api_key")
        val LATITUDE = floatPreferencesKey("latitude")
        val LONGITUDE = floatPreferencesKey("longitude")
    }

    private val settingsFlow = dataStore.data.catch {
        if (it is IOException) {
            Log.e("DATASTORE", "Error retrieving settings datastore.", it)
        } else {
            throw it // YEET
        }
    }

    override suspend fun getApiKey(): ApiKey {
        return settingsFlow.map { preferences ->
            ApiKey(preferences[StoreKeys.API_KEY] ?: "")
        }.catch {
            throw it
        }.firstOrNull() ?: ApiKey("")
    }

    override suspend fun getLocation(): Location {
        val lat = settingsFlow.map{ preferences ->
            preferences[StoreKeys.LATITUDE]
        }.catch {
            throw it
        }.firstOrNull() ?: 0.0f
        val long = settingsFlow.map{ preferences ->
            preferences[StoreKeys.LONGITUDE]
        }.catch {
            throw it
        }.firstOrNull() ?: 0.0f
        return Location(lat, long)
    }

    override suspend fun setApiKey(apiKey: ApiKey) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.API_KEY] = apiKey.key
        }
    }

    override suspend fun setLocation(location: Location) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.LATITUDE] = location.latitude
            preferences[StoreKeys.LONGITUDE] = location.longitude
        }
    }

}