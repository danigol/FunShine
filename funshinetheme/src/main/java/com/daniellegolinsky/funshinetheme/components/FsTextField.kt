package com.daniellegolinsky.funshinetheme.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshinetheme.designelements.getTextColor

/**
 * A text field wrapper that falls in line with the idea that control surfaces are on the surface
 *      of an object, like frosted glass or the glass of your smart device.
 */
@Composable
fun FsTextField(
    value: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        colors = TextFieldDefaults.colors(
            focusedTextColor = getTextColor(),
            disabledTextColor = getTextColor(),
            unfocusedTextColor = getTextColor(),
            errorTextColor = Color.Red, // TODO Design system stuff!
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        trailingIcon = trailingIcon,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        modifier = modifier,
        onValueChange = { onValueChange(it) }
    )
}

@Preview
@Composable
fun PreviewFsTextField() {
    FsTextField(value = "Oh, hey, put some text here!") { "" }
}