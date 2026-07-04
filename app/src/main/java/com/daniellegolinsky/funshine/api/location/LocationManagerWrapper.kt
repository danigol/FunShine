package com.daniellegolinsky.funshine.api.location

import android.Manifest
import android.location.LocationManager
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.RequiresPermission
import com.daniellegolinsky.funshine.FunshineApplication
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.LocationWrapperResult
import com.daniellegolinsky.funshine.usecase.GetLocationScaleUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executor
import java.util.function.Consumer

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
                    getLessAccurateLocation()
                }
            }
        } else {
            getLessAccurateLocation()
        }

        return locationFlow
    }

    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun getLessAccurateLocation() {
        var lastLoc =
            locationClient.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastLoc == null) {
            lastLoc =
                locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (lastLoc != null) {
            locationFlow.update { LocationWrapperResult.Success(lastLoc.toLocation()) }
        } else {
            locationFlow.update {
                LocationWrapperResult.Error(
                    "Could not get location. Try going outside, opening your maps app, and ensuring location permissions are turned on and active on your device."
                )
            }
        }
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
