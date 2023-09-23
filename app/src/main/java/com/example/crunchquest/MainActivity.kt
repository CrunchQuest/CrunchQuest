package com.example.crunchquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.crunchquest.ui.theme.CrunchQuestTheme
import kotlin.text.Typography.cent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrunchQuestTheme {

            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {

        }
        composable("discover") {

        }
        composable("social") {

        }
        composable("store") {

        }
        composable("leaderboard") {

        }
    }

}

@Composable
fun HomeScreen() {

}