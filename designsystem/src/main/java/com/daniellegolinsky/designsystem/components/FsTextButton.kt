package com.daniellegolinsky.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import com.daniellegolinsky.designsystem.designelements.getTextColor
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
import com.daniellegolinsky.designsystem.font.getButtonTextStyle

@Composable
fun FsTextButton(
    @StringRes buttonText: Int,
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
            text = stringResource(id = buttonText),
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
        buttonText = R.string.button_save_settings,
        onClick = {}
    )
}

object FsTextButtonDefaults {
    val BUTTON_HEIGHT = 64.dp
}