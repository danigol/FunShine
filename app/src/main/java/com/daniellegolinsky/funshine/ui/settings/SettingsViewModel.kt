package com.daniellegolinsky.funshine.ui.settings

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniellegolinsky.funshine.data.SettingsRepo
import com.daniellegolinsky.funshine.di.ApplicationModule
import com.daniellegolinsky.funshine.models.LengthUnit
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.SpeedUnit
import com.daniellegolinsky.funshine.models.TemperatureUnit
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshine.viewstates.settings.SettingsViewState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.absoluteValue

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo, @Named(
        ApplicationModule.IO_DISPATCHER
    ) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val tag = "SETTINGS_VIEW_MODEL"

    private var _settingsViewState: MutableStateFlow<ViewState<SettingsViewState>> =
            MutableStateFlow(ViewState.Loading())
    val settingsViewState: StateFlow<ViewState<SettingsViewState>> = _settingsViewState

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
        val grantedPermissionInPast = settingsRepo.getGrantedLocationPermissionBefore()
        val weatherButtonsOnRight = settingsRepo.getWeatherButtonsOnRight()

        _settingsViewState.value = mapSettingsToViewState(
            location = location,
            hasSeenLocationWarning = hasSeenLocationWarning,
            hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission,
            grantedPermissionInPast = grantedPermissionInPast,
            isFahrenheit = getIsFahrenheitFromDataStore(),
            isInch = getIsInchFromDataStore(),
            isMph = getIsMphFromDataStore(),
            weatherButtonsOnRight = weatherButtonsOnRight,
        )
    }

    fun setViewStateLocation(location: String) {
        _settingsViewState.value = updateViewState(location = sanitizeLocationString(location))
    }

    /**
     * These update both the view model and the data store, to ensure even if the user
     *  doesn't save settings, we still won't bother them again.
     */
    fun setHasSeenLocationWarning(hasSeenLocationWarning: Boolean) {
        _settingsViewState.value = updateViewState(hasSeenLocationWarning = hasSeenLocationWarning)
        viewModelScope.launch {
            settingsRepo.setHasSeenLocationWarning(hasSeenLocationWarning)
        }
    }

    /**
     * These update both the view model and the data store, to ensure even if the user
     *  doesn't save settings, we still won't bother them again.
     */
    fun setHasBeenPromptedForLocationPermission(hasBeenPrompted: Boolean) {
        _settingsViewState.value =
            updateViewState(hasBeenPromptedForLocationPermission = hasBeenPrompted)
        viewModelScope.launch {
            settingsRepo.setHasBeenPromptedForLocationPermission(hasBeenPrompted)
        }
    }

    private fun setIsLoadingLocation(isLoading: Boolean) {
        _settingsViewState.value = updateViewState(isLoadingLocation = isLoading)
    }

    private suspend fun setGrantedPermission(granted: Boolean) {
        settingsRepo.setGrantedLocationPermissionBefore(granted)
    }

    /*
     * Units are stored as enums, but we'll need booleans for the viewstate switches
     */
    fun setIsMph(isMph: Boolean) {
        _settingsViewState.value = updateViewState(isMph = isMph)
    }

    private suspend fun getIsMphFromDataStore(): Boolean {
        return when (settingsRepo.getSpeedUnit()) {
            SpeedUnit.KMH -> false
            else -> true
        }
    }

    fun setIsInch(isInch: Boolean) {
        _settingsViewState.value = updateViewState(isInch = isInch)
    }

    private suspend fun getIsInchFromDataStore(): Boolean {
        return when (settingsRepo.getLengthUnit()) {
            LengthUnit.MILLIMETER -> false
            else -> true
        }
    }

    fun setIsFahrenheit(isF: Boolean) {
        _settingsViewState.value = updateViewState(isFahrenheit = isF)
    }

    private suspend fun getIsFahrenheitFromDataStore(): Boolean {
        return when (settingsRepo.getTemperatureUnit()) {
            TemperatureUnit.CELSIUS -> false
            else -> true
        }
    }

    fun setWeatherButtonsOnRight(isOnRight: Boolean) {
        _settingsViewState.value = updateViewState(weatherButtonsOnRight = isOnRight)
    }

    fun saveSettings() {
        val settingsViewState = this._settingsViewState.value
        if (settingsViewState is ViewState.Success<SettingsViewState>) {
            saveStateToDatastore(settingsViewState)
        }
    }

    fun getIoDispatcher(): CoroutineDispatcher {
        return ioDispatcher
    }

    @SuppressLint("MissingPermission")
    fun getApproximateLocation(
        locationGranted: Boolean,
        locationClient: FusedLocationProviderClient
    ) {
        viewModelScope.launch(ioDispatcher) {
            // Ensure we're tracking response in datastore
            setGrantedPermission(locationGranted)
            // If granted location access, request it
            if (locationGranted) {
                setIsLoadingLocation(true)
                try {
                    locationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token,
                    ).addOnCompleteListener {
                        val locationResult = it?.result
                        val latitude = locationResult?.latitude?.toBigDecimal()
                            ?.setScale(2, RoundingMode.UP)?.toFloat() ?: 0.0f
                        val longitude = locationResult?.longitude?.toBigDecimal()
                            ?.setScale(2, RoundingMode.UP)?.toFloat() ?: 0.0f
                        setViewStateLocation("${latitude},${longitude}")
                        setIsLoadingLocation(false)
                    }
                } catch (e: Exception) {
                    // The only way this could be called is bad programmers calling this without permission
                    // Fortunately, Android will shut that down. This just prevents a crash.
                    e.printStackTrace()
                    setIsLoadingLocation(false)
                }
            }
        }
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
        return if (locString.isNotEmpty()
                    && locString.length > locString.indexOf(",")
                    && locString.indexOf(",") > 0 ) {
            val latString = locString.substring(0, locString.indexOf(",")).trim()
            val longString = locString
                .substring(locString.indexOf(","), locString.length)
                .replace(",", "").trim()
            if (!latString.isNullOrEmpty() && !longString.isNullOrEmpty()) {
                val latitude = latString.toFloat()
                val longitude = longString.toFloat()
                Location(latitude = latitude, longitude = longitude)
            } else {
                Location(0f, 0f)
            }
        } else {
            Location(0f, 0f)
        }
    }

    private fun getViewStateLocation(): String {
        return try {
            (_settingsViewState.value as ViewState.Success).data.latLong
        } catch(e: Exception) {
            Log.e(tag, "Lat/Lng accessed with non-success view state")
            "0.0,0.0"
        }
    }

    private fun getViewStateLocationWarning(): Boolean {
        return try {
            (_settingsViewState.value as ViewState.Success).data.hasSeenLocationWarning
        } catch(e: Exception) {
            Log.e(tag, "Has seen location warning accessed with non-success view state")
            false
        }
    }

    private fun getViewStatePromptedForLocationPermission(): Boolean {
        return try {
            (_settingsViewState.value as ViewState.Success).data.hasBeenPromptedForLocationPermission
        } catch(e: Exception) {
            Log.e(tag, "Has been prompted for location accessed with non-success view state")
            false
        }
    }

    private fun getViewStateIsFahrenheit(): Boolean {
        return try {
            (_settingsViewState.value as ViewState.Success).data.isFahrenheit
        } catch(e: Exception) {
            Log.e(tag, "Is Fahrenheit accessed with non-success view state")
            false
        }
    }

    private fun getViewStateIsMph(): Boolean {
        return try {
            (_settingsViewState.value as ViewState.Success).data.isMph
        } catch(e: Exception) {
            Log.e(tag, "Is MPH accessed with non-success view state")
            false
        }
    }

    private fun getViewStateIsInch(): Boolean {
        return try {
            (_settingsViewState.value as ViewState.Success).data.isInch
        } catch(e: Exception) {
            Log.e(tag, "Is Inch accessed with non-success view state")
            false
        }
    }

    private fun getViewStateButtonsOnRight(): Boolean {
        return try {
            (_settingsViewState.value as ViewState.Success).data.weatherButtonsOnRight
        } catch(e: Exception) {
            Log.e(tag, "Weather button location accessed with non-success view state")
            false
        }
    }

    private fun saveStateToDatastore(viewState: ViewState.Success<SettingsViewState>) {
        viewModelScope.launch {
            val location = generateLocationFromString(viewState.data.latLong)
            if (isValidLocation(location)) {
                settingsRepo.setLocation(location.latitude, location.longitude)
            }
            settingsRepo.setHasSeenLocationWarning(viewState.data.hasSeenLocationWarning)
            settingsRepo.setHasBeenPromptedForLocationPermission(viewState.data.hasBeenPromptedForLocationPermission)
            settingsRepo.setTemperatureUnit(viewState.data.isFahrenheit)
            settingsRepo.setLengthUnit(viewState.data.isInch)
            settingsRepo.setSpeedUnit(viewState.data.isMph)
            settingsRepo.setWeatherButtonsOnRight(viewState.data.weatherButtonsOnRight)
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
        grantedPermissionInPast: Boolean? = null,
        isLoadingLocation: Boolean? = null,
        isFahrenheit: Boolean? = null,
        isMph: Boolean? = null,
        isInch: Boolean? = null,
        weatherButtonsOnRight: Boolean? = null,
    ): ViewState.Success<SettingsViewState> {
        return ViewState.Success(
            SettingsViewState(
                latLong = location ?: getViewStateLocation(),
                hasSeenLocationWarning = hasSeenLocationWarning ?: getViewStateLocationWarning(),
                hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission
                    ?: getViewStatePromptedForLocationPermission(),
                grantedPermissionLastTime = grantedPermissionInPast ?: false,
                isLoadingLocation = isLoadingLocation ?: false,
                isFahrenheit = isFahrenheit ?: getViewStateIsFahrenheit(),
                isMph = isMph ?: getViewStateIsMph(),
                isInch = isInch ?: getViewStateIsInch(),
                weatherButtonsOnRight = weatherButtonsOnRight ?: getViewStateButtonsOnRight(),
            )
        )
    }

    private fun mapSettingsToViewState(
        location: Location,
        hasSeenLocationWarning: Boolean,
        hasBeenPromptedForLocationPermission: Boolean,
        grantedPermissionInPast: Boolean,
        isFahrenheit: Boolean,
        isMph: Boolean,
        isInch: Boolean,
        weatherButtonsOnRight: Boolean,
    ): ViewState.Success<SettingsViewState> {
        return ViewState.Success(
            SettingsViewState(
                latLong = "${location.latitude}, ${location.longitude}",
                hasSeenLocationWarning = hasSeenLocationWarning,
                hasBeenPromptedForLocationPermission = hasBeenPromptedForLocationPermission,
                grantedPermissionLastTime = grantedPermissionInPast,
                isFahrenheit = isFahrenheit,
                isMph = isMph,
                isInch = isInch,
                weatherButtonsOnRight = weatherButtonsOnRight,
            )
        )
    }
}
