package com.daniellegolinsky.funshine.usecase

import java.math.BigDecimal
import java.math.RoundingMode

class GetLocationScaleUseCase {
    operator fun invoke(location: BigDecimal): Float {
        return location.setScale(2, RoundingMode.UP)?.toFloat() ?: 0.0f
    }
}