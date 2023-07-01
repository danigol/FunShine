package com.daniellegolinsky.designsystem.font

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.designsystem.designelements.ThemeConstants
import com.daniellegolinsky.designsystem.designelements.getShadowAlpha
import com.daniellegolinsky.designsystem.font.FsTextStyle.BODY_FONT_SIZE
import com.daniellegolinsky.designsystem.font.FsTextStyle.BODY_FONT_WEIGHT
import com.daniellegolinsky.designsystem.font.FsTextStyle.HEADING_FONT_SIZE
import com.daniellegolinsky.designsystem.font.FsTextStyle.HEADING_FONT_WEIGHT

object FsTextStyle {
    const val BODY_FONT_WEIGHT: Int = 450
    const val HEADING_FONT_WEIGHT: Int = 600
    const val BODY_FONT_SIZE: Int = 16
    const val HEADING_FONT_SIZE: Int = 28
}

@Composable
fun getBodyFontStyle(): TextStyle {
    return typography.bodyLarge.copy(
        shadow = Shadow(
            color = colorResource(id = R.color.black).copy(alpha = getShadowAlpha()),
            offset = Offset(
                x = ThemeConstants.SHADOW_OFFSET_X_FLOAT,
                y = ThemeConstants.SHADOW_OFFSET_Y_FLOAT
            ),
            blurRadius = ThemeConstants.SHADOW_BLUR_RADIUS
        ),
        fontWeight = FontWeight(BODY_FONT_WEIGHT),
        fontSize = BODY_FONT_SIZE.sp
    )
}

@Composable
fun getHeadingFontStyle(): TextStyle {
    return typography.bodyLarge.copy(
        shadow = Shadow(
            color = colorResource(id = R.color.black).copy(alpha = getShadowAlpha()),
            offset = Offset(
                x = ThemeConstants.SHADOW_OFFSET_X_FLOAT,
                y = ThemeConstants.SHADOW_OFFSET_Y_FLOAT
            ),
            blurRadius = ThemeConstants.SHADOW_BLUR_RADIUS
        ),
        fontWeight = FontWeight(HEADING_FONT_WEIGHT),
        fontSize = HEADING_FONT_SIZE.sp
    )
}
