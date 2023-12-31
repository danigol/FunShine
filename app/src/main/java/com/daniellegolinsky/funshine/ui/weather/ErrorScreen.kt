package com.daniellegolinsky.funshine.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniellegolinsky.funshine.navigation.MainNavHost
import com.daniellegolinsky.funshine.viewstates.ViewState
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState
import com.daniellegolinsky.funshinetheme.components.FsIconWithShadow
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.components.FsTextButton
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.themeresources.R
import com.daniellegolinsky.themeresources.WeatherIconConstants

@Composable
fun ErrorScreen(
    viewState: ViewState.Error<WeatherScreenViewState>,
    navigateToSettings: () -> Unit,
    updateWeatherScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            FsIconWithShadow(
                image = painterResource(R.drawable.ic_circle_x_black),
                imageResourceContentDescription = stringResource(id = com.daniellegolinsky.funshine.R.string.wc_unknown),
                size = WeatherIconConstants.SIZE,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        FsText(
            text = viewState.errorString,
            textStyle = getBodyFontStyle(),
        )
        Spacer(modifier = Modifier.height(32.dp))
        FsTextButton(buttonText = stringResource(id = R.string.button_retry)) {
            updateWeatherScreen()
        }
        Spacer(modifier = Modifier.height(16.dp))
        FsTextButton(buttonText = stringResource(id = R.string.button_settings)) {
            navigateToSettings()
        }
        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Preview
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(
        viewState = ViewState.Error("This was a nasty error"),
        navigateToSettings = {},
        updateWeatherScreen = {},
    )
}
