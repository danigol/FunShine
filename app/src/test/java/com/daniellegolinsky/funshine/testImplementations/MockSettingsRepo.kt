package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.data.ISettingsRepo
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest

class MockSettingsRepo: ISettingsRepo {
    // TODO... yeah
    override suspend fun setLocation(lat: Float, long: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun getLocation(): Location {
        TODO("Not yet implemented")
    }

    override suspend fun setHasSeenLocationWarning(hasSeen: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getHasSeenLocationWarning(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getHasBeenPromptedForLocationPermission(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setGrantedLocationPermissionBefore(grantedPermission: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getGrantedLocationPermissionBefore(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setTemperatureUnit(isF: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getTemperatureUnit(): TemperatureUnit {
        TODO("Not yet implemented")
    }

    override suspend fun setLengthUnit(isIn: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getLengthUnit(): LengthUnit {
        TODO("Not yet implemented")
    }

    override suspend fun setSpeedUnit(isMph: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getSpeedUnit(): SpeedUnit {
        TODO("Not yet implemented")
    }

    override suspend fun setLastForecast(forecast: Forecast) {
        TODO("Not yet implemented")
    }

    override suspend fun getLastForecast(): Forecast? {
        TODO("Not yet implemented")
    }

    override suspend fun setLastRequest(weatherRequest: WeatherRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getLastRequest(): WeatherRequest? {
        TODO("Not yet implemented")
    }
}