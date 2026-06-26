package com.daniellegolinsky.funshine.api.location

import android.Manifest
import androidx.annotation.RequiresPermission
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.LocationWrapperResult
import com.daniellegolinsky.funshine.usecase.GetLocationScaleUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FusedLocationProviderWrapper(
    private val getLocationScaleUseCase: GetLocationScaleUseCase,
    private val locationClient: FusedLocationProviderClient,
) : LocationService {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getCurrentLocation(): Flow<LocationWrapperResult<Location?>> {
        val locationFlow: MutableStateFlow<LocationWrapperResult<Location?>> = MutableStateFlow(
            LocationWrapperResult.Loading()
        )
        locationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token,
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                val locationResult = it.result
                locationResult?.let { location ->
                    // Create a less-accurate version of the location
                    // Safer for protecting identities as much as we can with this data
                    val latitude = getLocationScaleUseCase(location.latitude.toBigDecimal())
                    val longitude =
                        getLocationScaleUseCase(location.longitude.toBigDecimal())
                    locationFlow.update {
                        LocationWrapperResult.Success(
                            Location(
                                latitude,
                                longitude,
                            )
                        )
                    }
                } ?: {
                    locationFlow.update {
                        LocationWrapperResult.Error(
                            "Location result empty, " +
                                    "try entering your latitude and longitude manually."
                        )
                    }
                }
            } else {
                locationFlow.update {
                    LocationWrapperResult.Error(
                        "Location API lookup failure, " +
                                "try entering your latitude and longitude manually."
                    )
                }
            }
        }
        return locationFlow
    }
}
