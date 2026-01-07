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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josphat.myakibabenki.navigation.NavigationRoutes
import com.josphat.myakibabenki.presentation.creategoal.CreateGoalScreen
import com.josphat.myakibabenki.presentation.home.HomeScreen
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAkibaBenkiApp()
                }
            }
        }
    }
}

@Composable
fun MyAkibaBenkiApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.HOME
    ) {
        composable(NavigationRoutes.HOME) {
            HomeScreen(
                onNavigateToCreateGoal = {
                    navController.navigate(NavigationRoutes.CREATE_GOAL)
                }
            )
        }

        composable(NavigationRoutes.CREATE_GOAL) {
            CreateGoalScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onGoalCreated = {
                    navController.popBackStack()
                }
            )
        }
    }
}