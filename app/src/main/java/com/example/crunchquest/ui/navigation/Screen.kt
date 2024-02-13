package com.example.crunchquest.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("home/{id}") {
        fun createRoute(id: Long) = "home/$id"
    }
    object Track : Screen("track")
    object Profile : Screen("profile")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}