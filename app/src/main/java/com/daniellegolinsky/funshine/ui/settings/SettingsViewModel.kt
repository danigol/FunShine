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
class SettingsViewModel @Inject constructor(val settingsRepo: SettingsRepo): ViewModel() {

    private val emptyState = SettingsViewState("", 0.0f, 0.0f)
    private var _settingsViewState: MutableStateFlow<SettingsViewState> = MutableStateFlow(emptyState)
    val settingsViewState: StateFlow<SettingsViewState> = _settingsViewState

    init {
        viewModelScope.launch {
            refreshSettings()
        }
    }

    suspend fun refreshSettings() {
        var apiKey = settingsRepo.getApiKey()
        val location = settingsRepo.getLocation()
        _settingsViewState.value = mapSettingsViewState(apiKey, location)
    }

    fun updateViewStateApiKey(apiKey: String) {
        _settingsViewState.value = mapSettingsViewState(ApiKey(apiKey), null)
    }

    fun updateLocation(locationString: String) {
        if (locationString.length > locationString.indexOf(",")) {
            val latString = locationString.substring(0, locationString.indexOf(",")).trim()
            val longString = locationString
                .substring(locationString.indexOf(","), locationString.length)
                .replace(",", "").trim()
            val latitude = latString.toFloat()
            val longitude = longString.toFloat()

            viewModelScope.launch {
                settingsRepo.setLocation(latitude, longitude)
                refreshSettings()
            }
        }
    }

    fun saveSettings() {
        saveStateToDatastore(this._settingsViewState.value)
    }

    private fun saveStateToDatastore(viewState: SettingsViewState) {
        viewModelScope.launch {
            settingsRepo.setApiKey(viewState.apiKey)
            settingsRepo.setLocation(viewState.latitude, viewState.longitude)
            refreshSettings()
        }
    }

    private fun mapSettingsViewState(apiKey: ApiKey?, location: Location?): SettingsViewState {
        val key = if (apiKey == null) {
            if (_settingsViewState.value.apiKey != null) {
                ApiKey(_settingsViewState.value.apiKey)
            } else {
                ApiKey(emptyState.apiKey)
            }
        } else {
            apiKey
        }
        val loc = if (location == null) {
            if (_settingsViewState.value.latitude != null && _settingsViewState.value.longitude != null) {
                Location(_settingsViewState.value.latitude, _settingsViewState.value.longitude)
            } else {
                Location(emptyState.latitude, emptyState.longitude)
            }
        } else {
            location
        }
        return SettingsViewState(
            apiKey = key.key,
            latitude = loc.latitude,
            longitude = loc.longitude
        )
    }
}
