package com.daniellegolinsky.funshine.api.location

import com.daniellegolinsky.funshine.models.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocationProvider @Inject constructor(
    val dispatcher: CoroutineDispatcher,
    val locationService: LocationService,
) {
    fun getLocation(): Flow<Location> {
        return flow {
            emit(locationService.getCurrentLocation())
        }.flowOn(dispatcher)
    }
}
