package com.example.baukur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.baukur.ui.screens.LoginScreen
import com.example.baukur.ui.screens.ProfileScreen
import com.example.baukur.ui.screens.RegisterScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    @Serializable
    object Login
    @Serializable
    object Register
    @Serializable
    object Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController();
            NavHost(navController = navController, startDestination = Login) {
                composable<Login> { LoginScreen(onNavigateToRegister = { navController.navigate(route = Register) }) }
                composable<Register> { RegisterScreen(onNavigateToLogin = { navController.navigate(route = Login) } ) }
                composable<Profile> { ProfileScreen(onNavigateToLogin = { navController.navigate(route = Login) }) }
            }

        }
    }
}