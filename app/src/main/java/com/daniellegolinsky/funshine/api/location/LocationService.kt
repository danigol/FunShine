package com.daniellegolinsky.funshine.api.location

import com.daniellegolinsky.funshine.models.Location
import com.daniellegolinsky.funshine.models.LocationWrapperResult
import kotlinx.coroutines.flow.Flow

interface LocationService {
    suspend fun getCurrentLocation(): Flow<LocationWrapperResult<Location?>>
}
