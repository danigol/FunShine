package com.daniellegolinsky.funshine.api.location

import android.location.LocationManager
import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.usecase.GetLocationScaleUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationManagerWrapper(
    private val getLocationScaleUseCase: GetLocationScaleUseCase,
    private val locationClient: LocationManager,
    private val dispatcher: CoroutineDispatcher,
): LocationService {
    override suspend fun getCurrentLocation(): Flow<Location?> {
        // TODO This will be put together after the fused location provider works
        return flow {
            emit(
                Location(
                    latitude = 1.23f,
                    longitude = 4.56f,
                )
            )
        }.flowOn(dispatcher)
    }
}
