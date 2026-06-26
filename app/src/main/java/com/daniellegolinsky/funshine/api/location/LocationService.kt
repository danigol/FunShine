package com.daniellegolinsky.funshine.api.location

import com.daniellegolinsky.funshine.models.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    suspend fun getCurrentLocation(): Flow<Location?>
}
