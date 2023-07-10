package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepo: SettingsRepo) : ViewModel() {

    private val emptyState = SettingsViewState("", "")
    private var _settingsViewState: MutableStateFlow<SettingsViewState> =
        MutableStateFlow(emptyState)
    val settingsViewState: StateFlow<SettingsViewState> = _settingsViewState

    init {
        viewModelScope.launch {
            updateViewStateFromDataStore()
        }
    }

    private suspend fun updateViewStateFromDataStore() {
        val apiKey = settingsRepo.getApiKey()
        val location = settingsRepo.getLocation()
        _settingsViewState.value = mapSettingsToViewState(apiKey, location)
    }

    fun updateViewStateApiKey(apiKey: String) {
        _settingsViewState.value = updateViewState(apiKey, null)
    }

    fun updateViewStateLocation(location: String) {
        _settingsViewState.value = updateViewState(null, sanitizeLocationString(location))
    }

    // Only allow digits, decimals, comma separators, or the negative sign. Will allow spaces
    private fun sanitizeLocationString(locationString: String): String {
        return locationString.filter { it.isDigit() || it == '.' || it == ',' || it == '-' || it == ' '}
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
            settingsRepo.setApiKey(viewState.apiKey)
            settingsRepo.setLocation(location.latitude, location.longitude)
        }
    }

    /**
     * Method for updating the view state
     * A null for either string will simply retain the existing value
     */
    private fun updateViewState(apiKey: String?, location: String?): SettingsViewState {
        return SettingsViewState(
            apiKey ?: _settingsViewState.value.apiKey,
            location ?: _settingsViewState.value.latLong
        )
    }

    private fun mapSettingsToViewState(apiKey: ApiKey, location: Location): SettingsViewState {
        return SettingsViewState(
            apiKey = apiKey.key,
            latLong = "${location.latitude}, ${location.longitude}"
        )
    }
}
