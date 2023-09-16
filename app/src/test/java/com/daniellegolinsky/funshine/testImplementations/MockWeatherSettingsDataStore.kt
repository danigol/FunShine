package com.daniellegolinsky.funshine.testImplementations

import com.daniellegolinsky.funshine.datastore.IWeatherSettingsDataStore
import com.daniellegolinsky.funshine.models.Forecast
import com.daniellegolinsky.funshine.models.ForecastTimestamp
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.models.api.WeatherRequest

class MockWeatherSettingsDataStore(
    private var location: Location = Location(0f, 0f),
    private var isImperial: Boolean = true,
    private var hasSeenLocation: Boolean = true,
    private var hasBeenPrompted: Boolean = true,
    private var permissionGranted: Boolean = true,
    private var apiCallCount: Int = 0,
    private var lastForecast: Forecast? = null,
    private var weatherRequest: WeatherRequest? = null,
    private var forecastTimestamp: ForecastTimestamp? = null,
): IWeatherSettingsDataStore {

    fun setApiCallCount(calls: Int) {
        this.apiCallCount = calls
    }

    override suspend fun getLocation(): Location {
        return this.location
    }

    override suspend fun setLocation(location: Location) {
        this.location = location
    }

    override suspend fun getHasSeenLocationWarning(): Boolean {
        return this.hasSeenLocation
    }

    override suspend fun setHasSeenLocationWarning(hasSeen: Boolean) {
        this.hasSeenLocation = hasSeen
    }

    override suspend fun getHasBeenPromptedForLocationPermission(): Boolean {
        return this.hasBeenPrompted
    }

    override suspend fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        this.hasBeenPrompted = hasBeenPrompted
    }

    override suspend fun getGrantedLocationPermissionBefore(): Boolean {
        return permissionGranted
    }

    override suspend fun setGrantedLocationPermissionBefore(hasGranted: Boolean) {
        permissionGranted = hasGranted
    }

    override suspend fun getTemperatureUnit(): TemperatureUnit {
        return if (this.isImperial) {
            TemperatureUnit.FAHRENHEIT
        } else {
            TemperatureUnit.CELSIUS
        }
    }

    override suspend fun setTemperatureUnit(isF: Boolean) {
        isImperial = isF
    }

    override suspend fun getLengthUnit(): LengthUnit {
        return if (this.isImperial) {
            LengthUnit.INCH
        } else {
            LengthUnit.MILLIMETER
        }
    }

    override suspend fun setLengthUnit(isInch: Boolean) {
        isImperial = isInch
    }

    override suspend fun getSpeedUnit(): SpeedUnit {
        return if (this.isImperial) {
            SpeedUnit.MPH
        } else {
            SpeedUnit.KMH
        }
    }

    override suspend fun setSpeedUnit(isMph: Boolean) {
        this.isImperial = isMph
    }

    override suspend fun getLastForecast(): Forecast? {
        return this.lastForecast
    }

    override suspend fun setLastForecast(forecast: Forecast) {
        this.lastForecast = forecast
    }

    override suspend fun getLastRequest(): WeatherRequest? {
        return this.weatherRequest
    }

    override suspend fun setLastRequest(weatherRequest: WeatherRequest) {
        this.weatherRequest = weatherRequest
    }

    override suspend fun getApiTimestamp(): ForecastTimestamp? {
        return this.forecastTimestamp
    }

    override suspend fun setNewApiTimestamp(forecastTimestamp: ForecastTimestamp) {
        this.forecastTimestamp = forecastTimestamp
    }

    override suspend fun getApiCallCount(): Int {
        return this.apiCallCount
    }

    override suspend fun incrementApiCallCount() {
        this.apiCallCount++
    }

    override suspend fun resetApiCallCount() {
        this.apiCallCount = 0
    }
}
