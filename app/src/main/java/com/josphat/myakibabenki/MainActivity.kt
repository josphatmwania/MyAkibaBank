package com.josphat.myakibabenki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.josphat.myakibabenki.navigation.NavGraph
import com.josphat.myakibabenki.navigation.Screen
import com.josphat.myakibabenki.ui.theme.MyAkibaBenkiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAkibaBenkiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MyAkibaBenkiApp()
                }
            }
        }
    }
}

/**
 * Main composable entry point for the application.
 * Sets up the navigation graph and theme.
 */
@Composable
fun MyAkibaBenkiApp() {
    val navController = rememberNavController()

    NavGraph(
        navController = navController,
        startDestination = Screen.Home.route
    )
}