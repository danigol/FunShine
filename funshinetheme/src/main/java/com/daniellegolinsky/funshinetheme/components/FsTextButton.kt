package com.daniellegolinsky.funshinetheme.components

import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshinetheme.R
import com.daniellegolinsky.funshinetheme.designelements.getForegroundItemColor
import com.daniellegolinsky.funshinetheme.designelements.getTextColor
import com.daniellegolinsky.funshinetheme.font.getButtonTextStyle

/**
 * FunShine sees data as projected and controls on the surface of a screen.
 * Think of it like data is a hologram above the screen, with controls on the screen.
 * Therefore, this button has no elevation. You can customize it with text.
 */
@Composable
fun FsTextButton(
    buttonText: String,
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
            .height(FsTextButtonDefaults.BUTTON_HEIGHT)
            .padding(0.dp),
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_button_top_left_black),
            contentDescription = null,
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(0.dp)
        )
        Text(
            text = buttonText,
            textAlign = TextAlign.Center,
            color = getTextColor(),
            maxLines = 1,
            style = getButtonTextStyle(),
            modifier = Modifier.padding(0.dp),
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_button_bottom_right_black),
            contentDescription = null,
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Preview
@Composable
fun PreviewFsTextButton() {
    FsTextButton(
        buttonText = "Save Settings",
        onClick = {}
    )
}

object FsTextButtonDefaults {
    val BUTTON_HEIGHT = 64.dp
}