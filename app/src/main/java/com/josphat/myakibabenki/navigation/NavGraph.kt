package com.josphat.myakibabenki.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.josphat.myakibabenki.presentation.creategoal.CreateGoalScreen
import com.josphat.myakibabenki.presentation.home.HomeScreen

private const val TAG = "NavGraph"

/**
 * Main navigation graph for the application.
 * Handles all navigation routes and screen compositions.
 *
 * @param navController The NavController managing navigation state
 * @param startDestination The initial screen to display (defaults to Home)
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCreateGoal = {
                    navigateSafely(navController, Screen.CreateGoal.route) {
                        Log.d(TAG, "Navigating to CreateGoal screen")
                    }
                }
            )
        }

        composable(route = Screen.CreateGoal.route) {
            CreateGoalScreen(
                onNavigateBack = {
                    navigateBackSafely(navController) {
                        Log.d(TAG, "Navigating back from CreateGoal screen")
                    }
                },
                onGoalCreated = {
                    navigateBackSafely(navController) {
                        Log.d(TAG, "Goal created, navigating back to Home")
                    }
                }
            )
        }

        composable(
            route = Screen.GoalDetail.route,
            arguments = Screen.GoalDetail.arguments
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getLong("goalId") ?: -1L
            if (goalId != -1L) {
                Log.d(TAG, "GoalDetail screen requested for goalId: $goalId")
            } else {
                Log.e(TAG, "Invalid goalId in GoalDetail route")
                navigateBackSafely(navController)
            }
        }
    }
}

private fun navigateSafely(
    navController: NavHostController,
    route: String,
    onSuccess: (() -> Unit)? = null
) {
    try {
        if (navController.currentDestination?.route != route) {
            navController.navigate(route)
            onSuccess?.invoke()
        } else {
            Log.w(TAG, "Already on route: $route")
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error navigating to route: $route", e)
    }
}

private fun navigateBackSafely(
    navController: NavHostController,
    onSuccess: (() -> Unit)? = null
) {
    try {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
            onSuccess?.invoke()
        } else {
            Log.w(TAG, "No back stack entry available")
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error navigating back", e)
    }
}

