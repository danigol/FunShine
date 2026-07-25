package com.daniellegolinsky.funshine.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshine.ui.components.FullScreenDialog
import com.daniellegolinsky.funshine.ui.getIsSmallDisplay
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle

@Composable
fun SettingsErrorDialog(
    errorText: String,
    modifier: Modifier = Modifier,
    dismissAction: () -> Unit,
) {
    val isOnSmallDisplay = getIsSmallDisplay(LocalConfiguration.current)
    FullScreenDialog(
        heading = stringResource(R.string.error_dialog_heading),
        buttonText = stringResource(R.string.error_dialog_dismiss),
        body = @Composable {
            FsText(
                text = errorText,
                textStyle = getBodyFontStyle(isOnSmallDisplay)
            )
        },
        modifier = modifier,
        buttonAction = dismissAction,
    )
}

@Preview
@Composable
fun PreviewSettingsErrorDialog(){
    SettingsErrorDialog(
        errorText = "This was an error"
    ) {}
}
