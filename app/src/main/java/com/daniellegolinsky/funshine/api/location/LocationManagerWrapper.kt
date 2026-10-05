package com.daniellegolinsky.funshine.api.location

import android.Manifest
import android.location.LocationManager
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresPermission
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.LocationWrapperResult
import com.daniellegolinsky.funshine.usecase.GetLocationScaleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executor

class LocationManagerWrapper(
    private val getLocationScaleUseCase: GetLocationScaleUseCase,
    private val locationClient: LocationManager,
    private val mainExecutor: Executor,
): LocationService {

    val locationFlow: MutableStateFlow<LocationWrapperResult<Location>> = MutableStateFlow(
        LocationWrapperResult.Loading()
    )

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation(): Flow<LocationWrapperResult<Location>> {

        // Only get the last known location if we can,
        // It's faster and usually accurate
        if (!getLastKnownLocation()) {
            // Try another way, if we can
            if (SDK_INT >= android.os.Build.VERSION_CODES.R) {
                locationClient.getCurrentLocation(
                    LocationManager.GPS_PROVIDER,
                    null,
                    mainExecutor,
                ) { location ->
                    if (location != null) {
                        locationFlow.update {
                            LocationWrapperResult.Success(location.toLocation())
                        }
                    } else {
                        informUserOfLocationError()
                    }
                }
            } else {
                informUserOfLocationError()
            }
        }

        return locationFlow
    }

    private fun informUserOfLocationError() {
        locationFlow.update {
            LocationWrapperResult.Error(
                "Could not get location. Try going outside, opening your maps app, and ensuring location permissions are turned on and active on your device."
            )
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun getLastKnownLocation(): Boolean {
        var lastLoc =
            locationClient.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastLoc == null) {
            lastLoc =
                locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (lastLoc != null) {
            locationFlow.update { LocationWrapperResult.Success(lastLoc.toLocation()) }
        }
        return lastLoc != null
    }

    fun android.location.Location.toLocation():  Location {
        return Location(
            latitude = getLocationScaleUseCase(
                this.latitude.toBigDecimal()
            ),
            longitude = getLocationScaleUseCase(
                this.longitude.toBigDecimal()
            )
        )
    }
}
