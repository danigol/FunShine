package com.daniellegolinsky.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.designsystem.designelements.ThemeConstants
import com.daniellegolinsky.designsystem.designelements.getShadowAlpha
import com.daniellegolinsky.designsystem.designelements.getTextColor

@Composable
fun FsText(
    text: String,
    maxLines: Int = 8, // TODO Make a constant/Style
    fontSize: TextUnit = 16.sp, // TODO Make a constant/style
    fontWeight: Int = 450,
    modifier: Modifier = Modifier
) {
    // TODO Consider just passing in a style, which would apply font size, maxlines, size, etc.
    Text(
        text,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        color = getTextColor(),
        maxLines = maxLines,
        style = MaterialTheme.typography.bodyLarge.copy(
            shadow = Shadow(
                color = colorResource(id = R.color.black).copy(alpha = getShadowAlpha()),
                offset = Offset(
                    x = ThemeConstants.SHADOW_OFFSET_X_FLOAT,
                    y = ThemeConstants.SHADOW_OFFSET_Y_FLOAT
                ),
                blurRadius = ThemeConstants.SHADOW_BLUR_RADIUS
            ),
            fontWeight = FontWeight(fontWeight)
        ),
        modifier = modifier,
    )
}

@Preview
@Composable
fun PreviewFsText() {
    FsText("This is some Funshine Text! Have fun in the sun! Or not!")
}