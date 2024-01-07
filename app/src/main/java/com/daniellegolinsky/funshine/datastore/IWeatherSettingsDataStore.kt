package com.daniellegolinsky.funshine.datastore

import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest

interface IWeatherSettingsDataStore {
    suspend fun getLocation(): Location
    suspend fun setLocation(location: Location)
    suspend fun getHasSeenLocationWarning(): Boolean
    suspend fun setHasSeenLocationWarning(hasSeen: Boolean)
    suspend fun getHasBeenPromptedForLocationPermission(): Boolean
    suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean)
    suspend fun getGrantedLocationPermissionBefore(): Boolean
    suspend fun setGrantedLocationPermissionBefore(hasGranted: Boolean)
    suspend fun getTemperatureUnit(): TemperatureUnit
    suspend fun setTemperatureUnit(isF: Boolean)
    suspend fun getLengthUnit(): LengthUnit
    suspend fun setLengthUnit(isInch: Boolean)
    suspend fun getSpeedUnit(): SpeedUnit
    suspend fun setSpeedUnit(isMph: Boolean)
    suspend fun getLastForecast(): Forecast?
    suspend fun setLastForecast(forecast: Forecast)
    suspend fun getLastRequest(): WeatherRequest?
    suspend fun setLastRequest(weatherRequest: WeatherRequest)
    suspend fun getApiTimestamp(): ForecastTimestamp?
    suspend fun setNewApiTimestamp(forecastTimestamp: ForecastTimestamp)
    suspend fun getApiCallCount(): Int
    suspend fun incrementApiCallCount()
    suspend fun resetApiCallCount()
    suspend fun getWeatherButtonsOnRight(): Boolean
    suspend fun setWeatherButtonsOnRight(isRight: Boolean)
    suspend fun getHasSeenSettingsHint(): Boolean
    suspend fun setHasSeenSettingsHint(hasSeen: Boolean)
}
