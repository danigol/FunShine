package com.daniellegolinsky.funshine.ui

import android.content.res.Configuration
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshinetheme.designelements.Shadow
import com.daniellegolinsky.funshinetheme.designelements.ShadowMatrix

object ScreenConstants {
    val SCREEN_PADDING = 16.dp
    val DOUBLE_SCREEN_PADDING = 32.dp
}

fun getShadowMatrix(isOnSmallDisplay: Boolean): ShadowMatrix?{
    return if (isOnSmallDisplay) {
        Shadow.getControlHintShadow()
    } else {
        null // Will go to defaults
    }
}

fun getIsSmallDisplay(config: Configuration): Boolean {
    val widthHeightRatio: Float =
        config.screenWidthDp.toFloat() / config.screenHeightDp.toFloat()

    return config.screenHeightDp < config.screenWidthDp
            || widthHeightRatio < 1.25f && widthHeightRatio > 0.75f // It's square-like
            || config.screenWidthDp > (config.screenHeightDp * 1.5) // It's landscape
}
