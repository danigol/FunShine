package com.daniellegolinsky.funshine.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniellegolinsky.funshine.api.RequestDatapoints
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
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
        val HAS_BEEN_PROMPTED_FOR_LOCATION = booleanPreferencesKey("hasBeenPromptedForLocation")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperatureUnit")
        val LENGTH_UNIT = stringPreferencesKey("lengthUnit")
        val SPEED_UNIT = stringPreferencesKey("speedUnit")
    }

    private val settingsFlow = dataStore.data.catch {
        if (it is IOException) {
            Log.e("DATASTORE", "Error retrieving settings datastore.", it)
        } else {
            throw it // YEET
        }
    }

    override suspend fun getLocation(): Location {
        val lat = settingsFlow.map { preferences ->
            preferences[StoreKeys.LATITUDE]
        }.catch {
            throw it
        }.firstOrNull() ?: 0.0f
        val long = settingsFlow.map { preferences ->
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

    override suspend fun getHasBeenPromptedForLocationPermission(): Boolean {
        return settingsFlow.map { preferences ->
            preferences[StoreKeys.HAS_BEEN_PROMPTED_FOR_LOCATION] ?: false
        }.firstOrNull() ?: false
    }

    override suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.HAS_BEEN_PROMPTED_FOR_LOCATION] = hasBeenPrompted
        }
    }

    override suspend fun getTemperatureUnit(): TemperatureUnit {
        val tempUnitAsString = settingsFlow.map { preferences ->
            preferences[StoreKeys.TEMPERATURE_UNIT] ?: ""
        }.firstOrNull() ?: ""
        return when (tempUnitAsString) {
            RequestDatapoints.C -> TemperatureUnit.CELSIUS
            else -> TemperatureUnit.FAHRENHEIT
        }
    }

    override suspend fun setTemperatureUnit(isF: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.TEMPERATURE_UNIT] =
                if (isF) TemperatureUnit.FAHRENHEIT.toString() else TemperatureUnit.CELSIUS.toString()
        }
    }

    override suspend fun getLengthUnit(): LengthUnit {
        val lengthUnitAsString = settingsFlow.map { preferences ->
            preferences[StoreKeys.LENGTH_UNIT] ?: ""
        }.firstOrNull() ?: ""
        return when (lengthUnitAsString) {
            RequestDatapoints.MM -> LengthUnit.MILLIMETER
            else -> LengthUnit.INCH
        }
    }

    override suspend fun setLengthUnit(isInch: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.LENGTH_UNIT] =
                if (isInch) LengthUnit.INCH.toString() else LengthUnit.MILLIMETER.toString()
        }
    }

    override suspend fun getSpeedUnit(): SpeedUnit {
        val speedUnitAsString = settingsFlow.map { preferences ->
            preferences[StoreKeys.SPEED_UNIT] ?: ""
        }.firstOrNull() ?: ""
        return when (speedUnitAsString) {
            RequestDatapoints.KMH -> SpeedUnit.KMH
            else -> SpeedUnit.MPH
        }
    }

    override suspend fun setSpeedUnit(isMph: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.SPEED_UNIT] =
                if (isMph) SpeedUnit.MPH.toString() else SpeedUnit.KMH.toString()
        }
    }
}
