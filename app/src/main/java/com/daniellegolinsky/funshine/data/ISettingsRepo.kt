package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest

interface ISettingsRepo {
    suspend fun setLocation(lat: Float, long: Float)
    suspend fun getLocation(): Location
    suspend fun setHasSeenLocationWarning(hasSeen: Boolean)
    suspend fun getHasSeenLocationWarning(): Boolean
    suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean)
    suspend fun getHasBeenPromptedForLocationPermission(): Boolean
    suspend fun setGrantedLocationPermissionBefore(grantedPermission: Boolean)
    suspend fun getGrantedLocationPermissionBefore(): Boolean
    suspend fun setTemperatureUnit(isF: Boolean)
    suspend fun getTemperatureUnit(): TemperatureUnit
    suspend fun setLengthUnit(isIn: Boolean)
    suspend fun getLengthUnit(): LengthUnit
    suspend fun setSpeedUnit(isMph: Boolean)
    suspend fun getSpeedUnit(): SpeedUnit
    suspend fun setLastForecast(forecast: Forecast)
    suspend fun getLastForecast(): Forecast?
    suspend fun setLastRequest(weatherRequest: WeatherRequest)
    suspend fun getLastRequest(): WeatherRequest?
}
