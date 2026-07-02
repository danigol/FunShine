package com.daniellegolinsky.funshine.ui

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
