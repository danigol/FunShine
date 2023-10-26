package com.daniellegolinsky.funshine.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daniellegolinsky.funshine.BuildConfig
import com.daniellegolinsky.funshine.api.RequestDatapoints
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONException
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
        val GRANTED_LOCATION_BEFORE = booleanPreferencesKey("grantedLocationBefore")
        val TEMPERATURE_UNIT = stringPreferencesKey("temperatureUnit")
        val LENGTH_UNIT = stringPreferencesKey("lengthUnit")
        val SPEED_UNIT = stringPreferencesKey("speedUnit")
        val FORECAST_KEY = stringPreferencesKey("forecast")
        val REQUEST_KEY = stringPreferencesKey("request")
        val API_CALL_COUNT = intPreferencesKey("api_call_count")
        val START_API_TIMESTAMP = stringPreferencesKey("start_api_timestamp")
        val VERSION_CODE_OF_FORECAST_CACHE = longPreferencesKey("version_code_of_forecast_cache")
        val WEATHER_BUTTONS_ON_RIGHT = booleanPreferencesKey("weather_buttons_on_right")
    }

    private val settingsFlow = dataStore.data.catch {
        if (it is IOException) {
            Log.e("DATASTORE", "Error retrieving settings datastore.", it)
        } else {
            throw it
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

    override suspend fun getGrantedLocationPermissionBefore(): Boolean {
        return settingsFlow.map { preferences ->
            preferences[StoreKeys.GRANTED_LOCATION_BEFORE]
        }.firstOrNull() ?: true
    }

    override suspend fun setGrantedLocationPermissionBefore(hasGranted: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.GRANTED_LOCATION_BEFORE] = hasGranted
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

    override suspend fun getLastForecast(): Forecast? {
        // If the version code for the forecast is outdated, return null to force a new request
        val storedForecastVersion = settingsFlow.map { preferences ->
            preferences[StoreKeys.VERSION_CODE_OF_FORECAST_CACHE]
        }.firstOrNull()

        if (storedForecastVersion == null
            || (storedForecastVersion != null && storedForecastVersion != BuildConfig.VERSION_CODE)) {
            return null
        }

        var forecast: Forecast? = null
        val jsonForecast = settingsFlow.map{ preferences ->
            preferences[StoreKeys.FORECAST_KEY]
        }.firstOrNull()

        jsonForecast?.let {
            try{
                forecast = Json.decodeFromString<Forecast>(it)
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }

        return forecast
    }
    override suspend fun setLastForecast(forecast: Forecast) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.FORECAST_KEY] = Json.encodeToString(forecast)
            preferences[StoreKeys.VERSION_CODE_OF_FORECAST_CACHE] = BuildConfig.VERSION_CODE
        }
    }

    override suspend fun getLastRequest(): WeatherRequest? {
        var lastRequest: WeatherRequest? = null
        val jsonRequest = settingsFlow.map{ preferences ->
            preferences[StoreKeys.REQUEST_KEY]
        }.firstOrNull()

        jsonRequest?.let {
            try{
                lastRequest = Json.decodeFromString<WeatherRequest>(it)
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }

        return lastRequest
    }

    override suspend fun setLastRequest(weatherRequest: WeatherRequest) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.REQUEST_KEY] = Json.encodeToString(weatherRequest)
        }
    }

    override suspend fun getApiTimestamp(): ForecastTimestamp? {
        var forecastTimestamp: ForecastTimestamp? = null
        val timestampJson = settingsFlow.map{ preferences ->
            preferences[StoreKeys.START_API_TIMESTAMP]
        }.firstOrNull()
        timestampJson?.let {
            try{
                forecastTimestamp = Json.decodeFromString<ForecastTimestamp>(it)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return forecastTimestamp
    }

    override suspend fun setNewApiTimestamp(forecastTimestamp: ForecastTimestamp) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.START_API_TIMESTAMP] = Json.encodeToString(forecastTimestamp)
        }
    }

    override suspend fun getApiCallCount(): Int {
        return settingsFlow.map { preferences ->
            preferences[StoreKeys.API_CALL_COUNT]
        }.firstOrNull() ?: 0
    }

    override suspend fun incrementApiCallCount() {
       val incrementCount = getApiCallCount() + 1
       dataStore.edit { preferences->
           preferences[StoreKeys.API_CALL_COUNT] = incrementCount
       }
    }

    /**
     * Only resets to 0, increment individually.
     */
    override suspend fun resetApiCallCount() {
        dataStore.edit { preferences->
            preferences[StoreKeys.API_CALL_COUNT] = 0
        }
    }

    override suspend fun getWeatherButtonsOnRight(): Boolean {
        return settingsFlow.map { preferences ->
            preferences[StoreKeys.WEATHER_BUTTONS_ON_RIGHT]
        }.firstOrNull() ?: true
    }

    override suspend fun setWeatherButtonsOnRight(isRight: Boolean) {
        dataStore.edit { preferences ->
            preferences[StoreKeys.WEATHER_BUTTONS_ON_RIGHT] = isRight
        }
    }
}
