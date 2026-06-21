package com.daniellegolinsky.funshine.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.daniellegolinsky.funshinetheme.components.FsText
import com.daniellegolinsky.funshinetheme.components.FsTextButton
import com.daniellegolinsky.funshinetheme.designelements.getBackgroundColor
import com.daniellegolinsky.funshinetheme.font.getBodyFontStyle
import com.daniellegolinsky.funshinetheme.font.getHeadingFontStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenDialog(
    heading: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    body: @Composable ColumnScope.() -> Unit,
    buttonAction: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(getBackgroundColor())
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        FsText(
            text = heading,
            textStyle = getHeadingFontStyle()
        )
        Spacer(modifier = Modifier.height(8.dp))

        body()
        Spacer(modifier = Modifier.weight(1f))

        FsTextButton(
            buttonText = buttonText
        ) {
            buttonAction.invoke()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun PreviewFullScreenDialog() {
    FullScreenDialog(
        heading = "Hello!",
        buttonText = "Good, thanks",
        body = @Composable{
            FsText(
                text = "How are you?",
                textStyle = getBodyFontStyle()
            )
        }
    ) { }
}