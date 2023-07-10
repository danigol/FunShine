package com.daniellegolinsky.designsystem.components

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.designsystem.designelements.getTextColor

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
        modifier = modifier,
        onValueChange = { onValueChange(it) }
    )
}

@Preview
@Composable
fun PreviewFsTextField() {
    FsTextField(value = "Oh, hey, put some text here!") { "" }
}