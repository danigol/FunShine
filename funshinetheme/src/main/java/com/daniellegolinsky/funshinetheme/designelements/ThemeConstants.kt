package com.daniellegolinsky.funshinetheme.designelements

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshinetheme.designelements.ThemeConstants.SHADOW_BLUR_RADIUS
import com.daniellegolinsky.funshinetheme.designelements.ThemeConstants.SHADOW_OFFSET_X_FLOAT
import com.daniellegolinsky.funshinetheme.designelements.ThemeConstants.SHADOW_OFFSET_Y_FLOAT

object ThemeConstants {
    const val SHADOW_OFFSET_X_FLOAT = 40.0f
    const val SHADOW_OFFSET_Y_FLOAT = 40.0f
    const val SHADOW_BLUR_RADIUS = 12.0f
}

// TODO these will eventually be defined differently to define degrees for a cast shadow
fun getShadowXOffset(): Dp {
    return SHADOW_OFFSET_X_FLOAT.dp
}

fun getShadowYOffset(): Dp {
    return SHADOW_OFFSET_Y_FLOAT.dp
}

fun getShadowBlurRadius(): Dp {
    return SHADOW_BLUR_RADIUS.dp
}