package com.daniellegolinsky.funshine.viewstates.settings

data class SettingsViewState(
    val latLong: String,
    val hasSeenLocationWarning: Boolean,
    val hasBeenPromptedForLocationPermission: Boolean,
    val grantedPermissionLastTime: Boolean,
    val isLoadingLocation: Boolean = false,
    val isFahrenheit: Boolean = true,
    val isMph: Boolean = true,
    val isInch: Boolean = true,
)
