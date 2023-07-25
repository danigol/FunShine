package com.daniellegolinsky.funshinetheme.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.designelements.getTextColor
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle

/**
 * Text, like data and images, is "projected" above the screen in FunShine.
 * Isn't that fun?
 * This will automatically cast a shadow for your text.
 */
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