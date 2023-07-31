package com.daniellegolinsky.funshine.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Location
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class WeatherSettingsDataStore @Inject constructor(
    @Named(ApplicationModule.SETTINGS_DATASTORE) val dataStore: DataStore<Preferences>
) : IWeatherSettingsDataStore {

    private object StoreKeys {
//        val API_KEY = stringPreferencesKey("api_key") // Don't remove, user may still have it in datastore
        val LATITUDE = floatPreferencesKey("latitude")
        val LONGITUDE = floatPreferencesKey("longitude")
        val HAS_SEEN_LOCATION_WARNING = booleanPreferencesKey("hasSeenLocationWarning")
    }

    private val settingsFlow = dataStore.data.catch {
        if (it is IOException) {
            Log.e("DATASTORE", "Error retrieving settings datastore.", it)
        } else {
            throw it // YEET
        }
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

    override suspend fun setLocation(location: Location) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.LATITUDE] = location.latitude
            preferences[StoreKeys.LONGITUDE] = location.longitude
        }
    }

    override suspend fun getHasSeenLocationWarning(): Boolean {
        return settingsFlow.map { preferences ->
            preferences[StoreKeys.HAS_SEEN_LOCATION_WARNING] ?: false
        }.firstOrNull() ?: false
    }

    override suspend fun setHasSeenLocationWarning(hasSeen: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.HAS_SEEN_LOCATION_WARNING] = hasSeen
        }
    }

}