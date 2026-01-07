package com.josphat.myakibabenki.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Sealed class representing all navigation destinations in the app.
 * Provides type-safe navigation with route definitions.
 */
sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    /**
     * Builds the full route with arguments if needed
     */
    fun createRoute(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    /**
     * Base route without arguments
     */
    val baseRoute: String
        get() = route.split("/").firstOrNull() ?: route

    // Home screen - Main entry point
    object Home : Screen("home")

    // Create Goal screen
    object CreateGoal : Screen("create_goal")

    // Goal Detail screen (for future use)
    object GoalDetail : Screen(
        route = "goal_detail/{goalId}",
        arguments = listOf(
            navArgument("goalId") {
                type = NavType.LongType
            }
        )
    ) {
        fun createRoute(goalId: Long): String = "goal_detail/$goalId"
    }
}

