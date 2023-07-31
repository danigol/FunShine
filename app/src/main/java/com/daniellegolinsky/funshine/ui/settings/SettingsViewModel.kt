package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.Location
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

    fun saveSettings() {
        saveStateToDatastore(this._settingsViewState.value)
    }

    fun setIsLoadingLocation(isLoading: Boolean) {
        _settingsViewState.value = updateViewState(
            location = null,
            hasSeenLocationWarning = null,
            hasBeenPromptedForLocationPermission = null,
            isLoading
        )
    }

    private fun saveStateToDatastore(viewState: SettingsViewState) {
        viewModelScope.launch {
            val location = generateLocationFromString(viewState.latLong)
            if (isValidLocation(location)) {
                settingsRepo.setLocation(location.latitude, location.longitude)
            }
            settingsRepo.setHasSeenLocationWarning(viewState.hasSeenLocationWarning)
            settingsRepo.setHasBeenPromptedForLocationPermission(viewState.hasBeenPromptedForLocationPermission)
        }
    }

    /**
     * Method for updating the view state
     * A null for either string will simply retain the existing value
     */
    private fun updateViewState(
        location: String?,
        hasSeenLocationWarning: Boolean?,
        hasBeenPromptedForLocationPermission: Boolean?,
        isLoadingLocation: Boolean? = null,
    ): SettingsViewState {
        return SettingsViewState(
            latLong = location ?: _settingsViewState.value.latLong,
            hasSeenLocationWarning = hasSeenLocationWarning
                ?: _settingsViewState.value.hasSeenLocationWarning,
            hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission
                ?: _settingsViewState.value.hasBeenPromptedForLocationPermission,
            isLoadingLocation = isLoadingLocation ?: false
        )
    }

    private fun mapSettingsToViewState(
        location: Location,
        hasSeenLocationWarning: Boolean,
        hasBeenPromptedForLocationPermission: Boolean,
    ): SettingsViewState {
        return SettingsViewState(
            latLong = "${location.latitude}, ${location.longitude}",
            hasSeenLocationWarning = hasSeenLocationWarning,
            hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission
        )
    }
}
