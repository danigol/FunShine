package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.absoluteValue

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo, @Named(
        ApplicationModule.IO_DISPATCHER
    ) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val emptyState = SettingsViewState(
        latLong = "",
        hasSeenLocationWarning = true,
        hasBeenPromptedForLocationPermission = false,
        isLoadingLocation = false,
    )
    private var _settingsViewState: MutableStateFlow<SettingsViewState> =
        MutableStateFlow(emptyState)
    val settingsViewState: StateFlow<SettingsViewState> = _settingsViewState

    init {
        viewModelScope.launch {
            updateViewStateFromDataStore()
        }
    }

    /**
     * Load data from data store to display on the view
     * Updates the view state flow and any views observing state
     */
    private suspend fun updateViewStateFromDataStore() {
        val location = settingsRepo.getLocation()
        val hasSeenLocationWarning = settingsRepo.getHasSeenLocationWarning()
        val hasBeenPromptedForLocationPermission =
            settingsRepo.getHasBeenPromptedForLocationPermission()
        _settingsViewState.value = mapSettingsToViewState(
            location,
            hasSeenLocationWarning,
            hasBeenPromptedForLocationPermission
        )
    }

    fun setViewStateLocation(location: String) {
        _settingsViewState.value = updateViewState(sanitizeLocationString(location), null, null)
    }

    fun setViewStateHasSeenLocationWarning(hasSeenLocationWarning: Boolean) {
        _settingsViewState.value = updateViewState(null, hasSeenLocationWarning, null)
    }

    fun setViewStateHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        _settingsViewState.value = updateViewState(
            location = null,
            hasSeenLocationWarning = null,
            hasBeenPromptedForLocationPermission = hasBeenPrompted
        )
    }

    fun setIsLoadingLocation(isLoading: Boolean) {
        _settingsViewState.value = updateViewState(
            location = null,
            hasSeenLocationWarning = null,
            hasBeenPromptedForLocationPermission = null,
            isLoading
        )
    }

    /*
     * Units are stored as enums, but we'll need booleans for the viewstate switches
     */
    fun setIsMph(isMph: Boolean) {
        _settingsViewState.value = updateViewState(isMph = isMph)
    }
    private fun updateViewStateSpeedUnit() {
        viewModelScope.launch {
            val speedUnit = settingsRepo.getSpeedUnit()
            _settingsViewState.value = updateViewState(
                isMph = when (speedUnit) {
                    SpeedUnit.KPH -> false
                    else -> true
                }
            )
        }
    }

    fun setIsInch(isInch: Boolean) {
        _settingsViewState.value = updateViewState(isInch = isInch)
    }
    fun updateViewStateLengthUnit() {
        TODO()
    }

    fun setIsFahrenheit(isF: Boolean) {
        _settingsViewState.value = updateViewState(isFahrenheit = isF)
    }
    fun updateViewStateTemperatureUnit() { // Gets from datastore to set in viewstate
        // TODO Could be private and change name to fetchIsFahrenheit?
        TODO()
    }

    /**
     * Get the temperature unit for the stored viewstate boolean
     */
    private fun getTempUnitFromViewState(): TemperatureUnit {
        return when(_settingsViewState.value.isFahrenheit) {
            true -> TemperatureUnit.FAHRENHEIT
            else -> TemperatureUnit.CELSIUS
        }
    }

    private fun getSpeedUnitFromViewState(): SpeedUnit {
        return when(_settingsViewState.value.isMph) {
            true -> SpeedUnit.MPH
            else -> SpeedUnit.KPH
        }
    }

    private fun getLengthUnitFromViewState(): LengthUnit {
        return when(_settingsViewState.value.isInch) {
            true -> LengthUnit.INCH
            else -> LengthUnit.CENTIMETER
        }
    }


    fun saveSettings() {
        saveStateToDatastore(this._settingsViewState.value)
    }

    fun getIoDispatcher(): CoroutineDispatcher {
        return ioDispatcher
    }

    // Only allow digits, decimals, comma separators, or the negative sign. Will allow spaces
    private fun sanitizeLocationString(locationString: String): String {
        return locationString.filter { it.isDigit() || it == '.' || it == ',' || it == '-' || it == ' ' }
    }

    /**
     * Verify the given location actually is a location, within the valid lat/long ranges
     * That means a latitude between -90 and 90, and a longitude between -180 and 180
     */
    private fun isValidLocation(location: Location): Boolean {
        return location.latitude.absoluteValue <= 90 && location.longitude.absoluteValue <= 180
    }

    /**
     * Data is still stored in an ApiKey/Location object format
     * ViewState is string-based, data storage retains type
     */
    private fun generateLocationFromString(locString: String): Location {
        return if (locString.length > locString.indexOf(",")) {
            val latString = locString.substring(0, locString.indexOf(",")).trim()
            val longString = locString
                .substring(locString.indexOf(","), locString.length)
                .replace(",", "").trim()
            val latitude = latString.toFloat()
            val longitude = longString.toFloat()
            Location(latitude = latitude, longitude = longitude)
        } else {
            Location(0f, 0f)
        }
    }

    private fun saveStateToDatastore(viewState: SettingsViewState) {
        // TODO Update with units
        viewModelScope.launch {
            val location = generateLocationFromString(viewState.latLong)
            if (isValidLocation(location)) {
                settingsRepo.setLocation(location.latitude, location.longitude)
            }
            settingsRepo.setHasSeenLocationWarning(viewState.hasSeenLocationWarning)
            settingsRepo.setHasBeenPromptedForLocationPermission(viewState.hasBeenPromptedForLocationPermission)
            settingsRepo.setTemperatureUnit(viewState.isFahrenheit)
            settingsRepo.setLengthUnit(viewState.isInch)
            settingsRepo.setSpeedUnit(viewState.isMph)
        }
    }

    /**
     * Method for updating the view state
     * A null for either string will simply retain the existing value
     */
    private fun updateViewState(
        location: String? = null,
        hasSeenLocationWarning: Boolean? = null,
        hasBeenPromptedForLocationPermission: Boolean? = null,
        isLoadingLocation: Boolean? = null,
        isFahrenheit: Boolean? = null,
        isMph: Boolean? = null,
        isInch: Boolean? = true,
    ): SettingsViewState {
        return SettingsViewState(
            latLong = location ?: _settingsViewState.value.latLong,
            hasSeenLocationWarning = hasSeenLocationWarning
                ?: _settingsViewState.value.hasSeenLocationWarning,
            hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission
                ?: _settingsViewState.value.hasBeenPromptedForLocationPermission,
            isLoadingLocation = isLoadingLocation ?: false,
            isFahrenheit = isFahrenheit ?: _settingsViewState.value.isFahrenheit,
            isMph = isMph ?: _settingsViewState.value.isMph,
            isInch = isInch ?: _settingsViewState.value.isInch,
        )
    }

    private fun mapSettingsToViewState(
        location: Location,
        hasSeenLocationWarning: Boolean,
        hasBeenPromptedForLocationPermission: Boolean,
    ): SettingsViewState {
        // TODO Update with units, call update method instead of returning directly
        return SettingsViewState(
            latLong = "${location.latitude}, ${location.longitude}",
            hasSeenLocationWarning = hasSeenLocationWarning,
            hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission
        )
    }
}
