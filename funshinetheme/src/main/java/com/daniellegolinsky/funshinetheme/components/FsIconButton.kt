package com.daniellegolinsky.funshinetheme.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshinetheme.R
import com.daniellegolinsky.funshinetheme.designelements.getForegroundItemColor

/**
 * Button with icon. Buttons in the FunShine design language are "on" the surface, no shadow
 * Provide null for the buttonIconContentDescription
 *      if you do not wish it to be read by screen readers. Otherwise, provide this for TalkBack
 */
@Composable
fun FsIconButton(
    buttonIcon: Painter,
    buttonIconContentDescription: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .height(FsButtonDefaults.BUTTON_HEIGHT)
            .padding(0.dp),
        onClick = { onClick() }
    ) {
        Icon(
            painter = buttonIcon,
            contentDescription = buttonIconContentDescription,
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Preview
@Composable
fun PreviewFsButtonCircle() {
    FsIconButton(
        buttonIcon = painterResource(id = R.drawable.ic_circle_black),
        buttonIconContentDescription = "A circle",
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewFsButtonCircleX() {
    FsIconButton(
        buttonIcon = painterResource(id = R.drawable.ic_circle_x_black),
        buttonIconContentDescription = "A circle with an 'x'",
        onClick = {}
    )
}

object FsButtonDefaults {
    val BUTTON_HEIGHT = 32.dp
}