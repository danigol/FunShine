package com.daniellegolinsky.funshine.ui.weather

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daniellegolinsky.themeresources.*
import com.daniellegolinsky.funshinetheme.components.FsIconButton
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.components.FsIconWithShadow
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle
import com.daniellegolinsky.funshine.navigation.MainNavHost
import com.daniellegolinsky.funshine.viewstates.weather.WeatherScreenViewState

@Composable
fun WeatherScreen(
    viewState: WeatherScreenViewState,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(32.dp) // TODO Obviously bad
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        FsIconWithShadow(
            image = painterResource(R.drawable.ic_sunny_black),
            imageResourceContentDescription = stringResource(R.string.ic_sunny_content_description),
        )
        FsText(
            text = "${viewState.temperature}ºF", // TODO Make this a string resource
            textStyle = getHeadingFontStyle(),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        FsText(
            text = viewState.forecast,
            textStyle = getBodyFontStyle(),
            maxLines = 8
        )
        Spacer(modifier = Modifier.weight(1f))
        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
                ){
            FsIconButton(
                buttonIcon = painterResource(id = R.drawable.ic_settings_button_black),
                buttonIconContentDescription = stringResource(id = R.string.ic_settings_button_content_description),
                onClick = {
                    navController.navigate(MainNavHost.SETTINGS)
                }
            )
            Spacer(modifier = Modifier.width(2.dp))
            // TODO: Refresh will re-load from server. Composable itself will be responsive
            FsIconButton(
                buttonIcon = painterResource(id = R.drawable.ic_refresh_button_black),
                buttonIconContentDescription = stringResource(id = R.string.ic_refresh_button_content_description),
                onClick = { Toast.makeText(context, "How refreshing!", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}

@Preview
@Composable
fun PreviewWeatherScreen() {
    WeatherScreen(
        WeatherScreenViewState(
            0,
            78,
            "It's going to be nice, all day, forever, just super nice."
        ),
        rememberNavController()
    )
}
