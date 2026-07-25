package com.daniellegolinsky.funshine.usecase

import java.math.BigDecimal
import java.math.RoundingMode

class GetLocationScaleUseCase {
    /**
     * Rounds the location values to 2 decimals, to reduce accuracy and protect privacy
     * It's obviously tempting to add a random value here between -0.02 and 0.02
     * That does ensure that the location returned is not as likely to be where someone is standing.
     * HOWEVER,
     *  One, it encourages bad behavior, like changing your location often to re-check the weather.
     *      This would lead to hitting the API limit by mid-day.
     *  Two, you could average out all the locations to almost better create a pinpoint of their spot,
     *      since the value is added pre rounding when the location is more unique.
     */
    operator fun invoke(location: BigDecimal): Float {
        return location.setScale(2, RoundingMode.UP)?.toFloat() ?: 0.0f
    }
}
