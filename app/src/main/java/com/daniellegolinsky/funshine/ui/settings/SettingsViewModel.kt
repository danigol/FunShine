package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

    fun updateApiKey(apiKey: String) {
        viewModelScope.launch {
            settingsRepo.setApiKey(apiKey)
            refreshSettings()
        }
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

    suspend fun saveSettings(apiKey: String, latitude: Float, longitude: Float) {
        settingsRepo.setApiKey(apiKey)
        settingsRepo.setLocation(latitude, longitude)
        refreshSettings()
    }

    private fun mapSettingsViewState(apiKey: ApiKey, location: Location): SettingsViewState {
        return SettingsViewState(
            apiKey = apiKey.toString(),
            latitude = location.latitude,
            longitude = location.longitude
        )
    }
}
