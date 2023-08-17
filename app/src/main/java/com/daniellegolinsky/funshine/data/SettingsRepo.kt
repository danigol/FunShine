package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.WeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest
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

    suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        dataStore.setHasBeenPromptedForLocationPermission(hasBeenPrompted)
    }
    suspend fun getHasBeenPromptedForLocationPermission(): Boolean {
        return dataStore.getHasBeenPromptedForLocationPermission()
    }

    suspend fun setTemperatureUnit(isF: Boolean) {
        dataStore.setTemperatureUnit(isF)
    }
    suspend fun getTemperatureUnit(): TemperatureUnit {
        return dataStore.getTemperatureUnit()
    }

    suspend fun setLengthUnit(isIn: Boolean) {
        dataStore.setLengthUnit(isIn)
    }
    suspend fun getLengthUnit(): LengthUnit {
        return dataStore.getLengthUnit()
    }

    suspend fun setSpeedUnit(isMph: Boolean) {
        dataStore.setSpeedUnit(isMph)
    }
    suspend fun getSpeedUnit(): SpeedUnit {
        return dataStore.getSpeedUnit()
    }

    suspend fun setLastForecast(forecast: Forecast) {
        dataStore.setLastForecast(forecast)
    }
    suspend fun getLastForecast(): Forecast? {
        return dataStore.getLastForecast()
    }

    suspend fun setLastRequest(weatherRequest: WeatherRequest) {
        dataStore.setLastRequest(weatherRequest)
    }
    suspend fun getLastRequest(): WeatherRequest? {
        return dataStore.getLastRequest()
    }
}
