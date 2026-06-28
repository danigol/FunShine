package com.daniellegolinsky.funshine.api.location

import android.Manifest
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.LocationWrapperResult
import com.daniellegolinsky.funshine.usecase.GetLocationScaleUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class LocationManagerWrapper(
    private val getLocationScaleUseCase: GetLocationScaleUseCase,
    private val locationClient: LocationManager,
    private val dispatcher: CoroutineDispatcher,
): LocationService {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation(): Flow<LocationWrapperResult<Location?>> {
        // TODO This will be put together after the fused location provider works
        val locationFlow: MutableStateFlow<LocationWrapperResult<Location?>> = MutableStateFlow(
            LocationWrapperResult.Loading()
        )

        val lastLoc =
            locationClient.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.toLocation()
        if (lastLoc != null) {
            locationFlow.update { LocationWrapperResult.Success(lastLoc) }
        } else {
            // TODO Can try to do get last known location first, and if it has nothing, do the rest
            locationFlow.update {
                LocationWrapperResult.Success(
                    Location(
                        latitude = 1.23f,
                        longitude = 4.56f,
                    )
                )
            }
        }
        return locationFlow
    }

    fun android.location.Location.toLocation(): Location {
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
