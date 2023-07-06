package com.daniellegolinsky.designsystem.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.designsystem.R
import com.daniellegolinsky.designsystem.designelements.getForegroundItemColor

@Composable
fun FsIconButton(
    @DrawableRes buttonIcon: Int,
    @StringRes buttonIconContentDescription: Int,
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
        modifier = modifier.height(FsButtonDefaults.BUTTON_HEIGHT).padding(0.dp),
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(id = buttonIcon),
            contentDescription = stringResource(buttonIconContentDescription),
            tint = getForegroundItemColor(),
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Preview
@Composable
fun PreviewFsButtonRefresh() {
    FsIconButton(
        buttonIcon = R.drawable.ic_refresh_button_black,
        buttonIconContentDescription = R.string.ic_refresh_button_content_description,
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewFsButtonSettings() {
    FsIconButton(
        buttonIcon = R.drawable.ic_settings_button_black,
        buttonIconContentDescription = R.string.ic_settings_button_content_description,
        onClick = {}
    )
}

object FsButtonDefaults {
    val BUTTON_HEIGHT = 32.dp
}