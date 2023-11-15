package com.daniellegolinsky.funshine.ui.weather

import android.content.Context
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniellegolinsky.themeresources.*
import com.daniellegolinsky.funshinetheme.components.FsIconButton
import com.daniellegolinsky.funshine.viewstates.ViewState

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewState = viewModel.weatherViewState.collectAsStateWithLifecycle().value
    val localContext = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        when (viewState) {
            is ViewState.Loading -> {
                LoadingScreen(modifier)
            }

            is ViewState.Error -> {
                ErrorScreen(
                    viewState = viewState,
                    navigateToSettings = navigateToSettings,
                    updateWeatherScreen = { viewModel.updateWeatherScreen() }
                )
            }

            is ViewState.Success -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    /* TODO
                     *  May want to use foldable methods here with
                     *      WindowInfoTracker on the containing activity
                     *  But using recomposition from folding/unfolding to swap these out also works
                     */
                    val config = LocalConfiguration.current
                    val widthHeightRatio: Float = config.screenWidthDp.toFloat() / config.screenHeightDp.toFloat()
                    if (config.screenHeightDp < config.screenWidthDp
                        || widthHeightRatio < 1.25f && widthHeightRatio > 0.75f // It's square-like
                        || config.screenWidthDp > (config.screenHeightDp * 1.5) // It's landscape
                    ) {
                        WeatherComponentSmall(viewState = viewState)
                    } else {
                        WeatherComponent(viewState = viewState)
                    }
                }
                // Controls (TODO: Split this out too)
                Row(
                    horizontalArrangement = if (viewState.data.buttonsOnRight) Arrangement.End else Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp) // TODO Make this a constant
                ) {
                    FsIconButton(
                        buttonIcon = painterResource(id = R.drawable.ic_settings_button_black),
                        buttonIconContentDescription = stringResource(id = R.string.ic_settings_button_content_description),
                        onClick = {
                            navigateToSettings()
                        }
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    FsIconButton(
                        buttonIcon = painterResource(id = R.drawable.ic_refresh_button_black),
                        buttonIconContentDescription = stringResource(id = R.string.ic_refresh_button_content_description),
                        onClick = {
                            // Everything loads too fast for feedback on the refresh button tap
                            // This will read out that it's refreshing with Toast (very high) priority
                            val accessibilityService =
                                localContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
                            val accessibilityEnabled =
                                accessibilityService != null && accessibilityService.isEnabled
                            if (accessibilityEnabled) {
                                val refreshMessage =
                                    localContext.getString(R.string.refresh_button_updating_message)
                                Toast.makeText(localContext, refreshMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            viewModel.updateWeatherScreen()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWeatherScreen() {
    WeatherScreen(
        viewModel(),
        navigateToSettings = {},
    )
}
