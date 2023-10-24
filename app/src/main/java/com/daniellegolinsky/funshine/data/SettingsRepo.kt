package com.daniellegolinsky.funshine.data

import com.daniellegolinsky.funshine.datastore.IWeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest
import javax.inject.Inject

class SettingsRepo @Inject constructor(
    private val dataStore: IWeatherSettingsDataStore,
) : ISettingsRepo {
    override suspend fun setLocation(lat: Float, long: Float) {
        dataStore.setLocation(Location(lat, long))
    }

    override suspend fun getLocation(): Location {
        return dataStore.getLocation()
    }

    override suspend fun setHasSeenLocationWarning(hasSeen: Boolean) {
        dataStore.setHasSeenLocationWarning(hasSeen)
    }
    override suspend fun getHasSeenLocationWarning(): Boolean {
        return dataStore.getHasSeenLocationWarning()
    }

    override suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        dataStore.setHasBeenPromptedForLocationPermission(hasBeenPrompted)
    }
    override suspend fun getHasBeenPromptedForLocationPermission(): Boolean {
        return dataStore.getHasBeenPromptedForLocationPermission()
    }

    override suspend fun setGrantedLocationPermissionBefore(grantedPermission: Boolean) {
        dataStore.setGrantedLocationPermissionBefore(grantedPermission)
    }
    override suspend fun getGrantedLocationPermissionBefore(): Boolean {
        return dataStore.getGrantedLocationPermissionBefore()
    }

    override suspend fun setTemperatureUnit(isF: Boolean) {
        dataStore.setTemperatureUnit(isF)
    }
    override suspend fun getTemperatureUnit(): TemperatureUnit {
        return dataStore.getTemperatureUnit()
    }

    override suspend fun setLengthUnit(isIn: Boolean) {
        dataStore.setLengthUnit(isIn)
    }
    override suspend fun getLengthUnit(): LengthUnit {
        return dataStore.getLengthUnit()
    }

    override suspend fun setSpeedUnit(isMph: Boolean) {
        dataStore.setSpeedUnit(isMph)
    }
    override suspend fun getSpeedUnit(): SpeedUnit {
        return dataStore.getSpeedUnit()
    }

    override suspend fun setLastForecast(forecast: Forecast) {
        dataStore.setLastForecast(forecast)
    }
    override suspend fun getLastForecast(): Forecast? {
        return dataStore.getLastForecast()
    }

    override suspend fun setLastRequest(weatherRequest: WeatherRequest) {
        dataStore.setLastRequest(weatherRequest)
    }
    override suspend fun getLastRequest(): WeatherRequest? {
        return dataStore.getLastRequest()
    }
}
