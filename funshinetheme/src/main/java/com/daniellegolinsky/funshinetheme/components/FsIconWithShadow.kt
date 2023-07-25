package com.daniellegolinsky.funshinetheme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.R
import com.daniellegolinsky.funshinetheme.designelements.getForegroundItemColor
import com.daniellegolinsky.funshinetheme.designelements.getShadowAlpha
import com.daniellegolinsky.funshinetheme.designelements.getShadowBlurRadius
import com.daniellegolinsky.funshinetheme.designelements.getShadowXOffset
import com.daniellegolinsky.funshinetheme.designelements.getShadowYOffset

/**
 * In the FunShine design language, data is "projected" above the screen. Like a physical hologram
 * It therefore casts a shadow. This allows you to make shadows for your drawables, for any shape
 * Please provide a content description with `imageResourceContentDescription`,
 *      make this null if it is not to be read out and purely for decoration only.
 */
@Composable
fun FsIconWithShadow(
    image: Painter,
    imageResourceContentDescription: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Icon(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .alpha(alpha = getShadowAlpha())
                .offset(
                    x = getShadowXOffset(),
                    y = getShadowYOffset()
                ) // TODO This will be an angle and customizable
                .blur(radius = getShadowBlurRadius())
                .padding(maxOf(getShadowXOffset(), getShadowYOffset())),
        )
        Icon(
            painter = image,
            contentDescription = imageResourceContentDescription,
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(maxOf(getShadowXOffset(), getShadowYOffset())),
        )
    }
}

@Preview
@Composable
fun PreviewWeatherStatusImage() {
    FsIconWithShadow(
        image = painterResource(id = R.drawable.ic_circle_black),
        imageResourceContentDescription = "Circle"
    )
}