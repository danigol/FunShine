package com.daniellegolinsky.funshine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.daniellegolinsky.designsystem.designelements.getBackgroundColor
import com.daniellegolinsky.funshine.databinding.ActivityMainBinding
import com.daniellegolinsky.funshine.navigation.MainNavHost

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(getBackgroundColor())) {// TODO, theme dependent
                MainNavHost(destination = MainNavHost.WEATHER)
            }
        }
    }
}
