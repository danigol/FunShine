package com.daniellegolinsky.funshine.ui.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshinetheme.components.FsLocationButton
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle

@Composable
fun LocationPermissionInfoDialog(
    modifier: Modifier = Modifier
) {
    val showAlert = remember { mutableStateOf(true) }
    val uriHandler = LocalUriHandler.current
    val openMeteoTermsLink = stringResource(id = R.string.open_meteo_terms_link)

    if (showAlert.value) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            FsText(
                text = stringResource(id = R.string.welcome_to_funshine),
                textStyle = getHeadingFontStyle()
            )
            Spacer(modifier = Modifier.height(16.dp))
            FsText(
                text = stringResource(id = R.string.location_button_introduction),
                textStyle = getBodyFontStyle()
            )
            FsLocationButton {}
            FsText(
                text = stringResource(id = R.string.location_explanation),
                maxLines = 31,
                textStyle = getBodyFontStyle()
            )
            Spacer(modifier = Modifier.height(8.dp))
            FsText(
                text = openMeteoTermsLink,
                textStyle = getBodyFontStyle(),
                modifier = Modifier.clickable(role = Role.Button) {
                    uriHandler.openUri(openMeteoTermsLink)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun PreviewLocationPermissionInfoDialog() {
    LocationPermissionInfoDialog()
}
