package com.daniellegolinsky.designsystem.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.designsystem.designelements.getTextColor
import com.daniellegolinsky.designsystem.font.getBodyFontStyle

@Composable
fun FsText(
    text: String,
    maxLines: Int = 8, // TODO Make a constant/Style
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        textAlign = TextAlign.Center,
        color = getTextColor(),
        maxLines = maxLines,
        style = textStyle,
        modifier = modifier,
    )
}

@Preview
@Composable
fun PreviewFsText() {
    FsText(
        text = "This is some Funshine Text! Have fun in the sun! Or not!",
        textStyle = getBodyFontStyle()
    )
}