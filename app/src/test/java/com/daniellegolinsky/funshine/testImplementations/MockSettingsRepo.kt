package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.data.ISettingsRepo
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest

class MockSettingsRepo: ISettingsRepo {

    private var location: Location = Location(0f, 0f)
    private var hasSeenLocationWarning = false
    private var hasBeenPrompted = false
    private var grantedPermissionBefore = false
    private var tempUnitIsF = false
    private var isInch = false
    private var isMph = false

    override suspend fun setLocation(lat: Float, long: Float) {
        location = Location(lat, long)
    }

    override suspend fun getLocation(): Location {
        return location
    }

    override suspend fun setHasSeenLocationWarning(hasSeen: Boolean) {
        hasSeenLocationWarning = hasSeen
    }

    override suspend fun getHasSeenLocationWarning(): Boolean {
        return hasSeenLocationWarning
    }

    override suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        this.hasBeenPrompted = hasBeenPrompted
    }

    override suspend fun getHasBeenPromptedForLocationPermission(): Boolean {
        return hasBeenPrompted
    }

    override suspend fun setGrantedLocationPermissionBefore(grantedPermission: Boolean) {
        grantedPermissionBefore = grantedPermission
    }

    override suspend fun getGrantedLocationPermissionBefore(): Boolean {
        return grantedPermissionBefore
    }

    override suspend fun setTemperatureUnit(isF: Boolean) {
        tempUnitIsF = isF
    }

    override suspend fun getTemperatureUnit(): TemperatureUnit {
        return if (tempUnitIsF) {
            TemperatureUnit.FAHRENHEIT
        } else {
            TemperatureUnit.CELSIUS
        }
    }

    override suspend fun setLengthUnit(isIn: Boolean) {
        isInch = isIn
    }

    override suspend fun getLengthUnit(): LengthUnit {
        return if (isInch) {
            LengthUnit.INCH
        } else {
            LengthUnit.MILLIMETER
        }
    }

    override suspend fun setSpeedUnit(isMph: Boolean) {
        this.isMph = isMph
    }

    override suspend fun getSpeedUnit(): SpeedUnit {
        return if (isMph) {
           SpeedUnit.MPH
        } else {
            SpeedUnit.KMH
        }
    }

    // TODO These are more complex, may use them for API testing though
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