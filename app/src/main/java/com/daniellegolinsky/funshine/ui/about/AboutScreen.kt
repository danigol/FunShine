package com.daniellegolinsky.funshine.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniellegolinsky.funshine.BuildConfig
import com.daniellegolinsky.funshine.R
import com.daniellegolinsky.funshine.ui.ScreenConstants
import com.daniellegolinsky.funshinetheme.components.FsAppBar
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle

@Composable
fun AboutScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    val uriHandler = LocalUriHandler.current
    val funshinePrivacyLink = stringResource(id = R.string.funshine_privacy_link)
    val openMeteoTermsLink = stringResource(id = R.string.open_meteo_terms_link)
    val version = BuildConfig.VERSION_NAME

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(top = ScreenConstants.SCREEN_PADDING)
    ) {
        FsAppBar(headingText = stringResource(id = R.string.settings_about)) {
            navigateUp()
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
            FsText(
                text = stringResource(id = R.string.about_information),
                textStyle = getBodyFontStyle(),
                maxLines = 15,
                modifier = Modifier.padding(horizontal = ScreenConstants.DOUBLE_SCREEN_PADDING)
            )
            Spacer(modifier = Modifier.height(12.dp))
            FsText(
                text = stringResource(id = R.string.funshine_privacy),
                textStyle = getHeadingFontStyle(),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(role = Role.Button) {
                    uriHandler.openUri(funshinePrivacyLink)
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            FsText(
                text = stringResource(id = R.string.about_information_2),
                textStyle = getBodyFontStyle(),
                maxLines = 15,
                modifier = Modifier.padding(horizontal = ScreenConstants.DOUBLE_SCREEN_PADDING)
            )
            Spacer(modifier = Modifier.height(12.dp))
            FsText(
                text = stringResource(id = R.string.open_meteo_terms),
                textStyle = getHeadingFontStyle(),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(role = Role.Button) {
                    uriHandler.openUri(openMeteoTermsLink)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            FsText(text = stringResource(id = R.string.version) + " $version", textStyle = getBodyFontStyle())
            Spacer(modifier = Modifier.height(96.dp)) // End spacing for foldable outer screens
        }
    }
}

@Preview
@Composable
fun PreviewAboutScreen() {
    AboutScreen({})
}
