package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepo: SettingsRepo) : ViewModel() {

    private val emptyState = SettingsViewState(
        latLong = "",
        hasSeenLocationWarning = true
    )
    private var _settingsViewState: MutableStateFlow<SettingsViewState> =
        MutableStateFlow(emptyState)
    val settingsViewState: StateFlow<SettingsViewState> = _settingsViewState

    init {
        viewModelScope.launch {
            updateViewStateFromDataStore()
        }
    }

    private suspend fun updateViewStateFromDataStore() {
        val location = settingsRepo.getLocation()
        val hasSeenLocationWarning = settingsRepo.getHasSeenLocationWarning()
        _settingsViewState.value = mapSettingsToViewState(location, hasSeenLocationWarning)
    }

    fun setViewStateLocation(location: String) {
        _settingsViewState.value = updateViewState(sanitizeLocationString(location), null)
    }

    fun setViewStateHasSeenLocationWarning(hasSeenLocationWarning: Boolean) {
        _settingsViewState.value = updateViewState(null, hasSeenLocationWarning)
    }

    // Only allow digits, decimals, comma separators, or the negative sign. Will allow spaces
    private fun sanitizeLocationString(locationString: String): String {
        return locationString.filter { it.isDigit() || it == '.' || it == ',' || it == '-' || it == ' '}
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

    fun updateLocation(locationString: String) {
        val loc = generateLocationFromString(locationString)
        viewModelScope.launch {
            settingsRepo.setLocation(loc.latitude, loc.longitude)
        }
    }

    fun saveSettings() {
        saveStateToDatastore(this._settingsViewState.value)
    }

    private fun saveStateToDatastore(viewState: SettingsViewState) {
        viewModelScope.launch {
            val location = generateLocationFromString(viewState.latLong)
            if (isValidLocation(location)) {
                settingsRepo.setLocation(location.latitude, location.longitude)
            }
            settingsRepo.setHasSeenLocationWarning(viewState.hasSeenLocationWarning)
        }
    }

    /**
     * Method for updating the view state
     * A null for either string will simply retain the existing value
     */
    private fun updateViewState(location: String?, hasSeenLocationWarning: Boolean?): SettingsViewState {
        return SettingsViewState(
            latLong = location ?: _settingsViewState.value.latLong,
            hasSeenLocationWarning = hasSeenLocationWarning ?: _settingsViewState.value.hasSeenLocationWarning
        )
    }

    private fun mapSettingsToViewState(location: Location, hasSeenLocationWarning: Boolean): SettingsViewState {
        return SettingsViewState(
            latLong = "${location.latitude}, ${location.longitude}",
            hasSeenLocationWarning = hasSeenLocationWarning
        )
    }
}
