package com.briefly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.briefly.ui.navigation.BrieflyNavigation
import com.briefly.ui.screen.splashscreen.BrieflySplashScreen
import com.briefly.ui.theme.BrieflyTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrieflyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BrieflyScreens(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BrieflyScreens(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    BrieflyNavigation(navController)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrieflyTheme {
        BrieflyScreens()
    }
}