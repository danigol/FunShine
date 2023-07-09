package com.daniellegolinsky.funshine.ui.settings

import androidx.lifecycle.ViewModel
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.models.ApiKey
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val settingsRepo: SettingsRepo): ViewModel() {

    private var _settingsViewState: MutableStateFlow<SettingsViewState?> = MutableStateFlow(null)
    val settingsViewState: StateFlow<SettingsViewState?> = _settingsViewState
    suspend fun refreshSettings() {
        var apiKey = settingsRepo.getApiKey()
        val location = settingsRepo.getLocation()
        _settingsViewState.value = mapSettingsViewState(apiKey, location)
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
