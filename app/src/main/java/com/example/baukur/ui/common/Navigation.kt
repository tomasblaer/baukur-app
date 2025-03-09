package com.example.baukur.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.baukur.ui.screens.HomeScreen
import com.example.baukur.ui.screens.LoginScreen
import com.example.baukur.ui.screens.ProfileScreen
import com.example.baukur.ui.screens.RegisterScreen
import com.example.baukur.ui.screens.expenses.AddExpenseScreen
import com.example.baukur.ui.theme.BaukurTheme
import kotlinx.serialization.Serializable

@Serializable
object Login
@Serializable
object Register
@Serializable
object Profile
@Serializable
object Home
@Serializable
object AddExpense

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Home", Home, Icons.Default.Home),
    TopLevelRoute("Expenses", AddExpense, Icons.Default.AddCircle),
    TopLevelRoute("Profile", Profile, Icons.Default.Person),
)

@Composable
fun Navigation() {
    val navController = rememberNavController()
    BaukurTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFFAF3F3),
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    topLevelRoutes.forEach { route ->
                        NavigationBarItem(
                            icon = { route.icon },
                            label = { route.name },
                            selected = currentDestination?.hierarchy?.any {
                                it.hasRoute(
                                    route.route::class
                                )
                            } == true,
                            onClick = {
                                navController.navigate(route.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Login,
                Modifier.padding(innerPadding)
            ) {
                composable<Login> {
                    LoginScreen(
                        onNavigateToRegister = { navController.navigate(route = Register) },
                        onNavigateToHome = { navController.navigate(route = Home) }
                    )
                }
                composable<Register> {
                    RegisterScreen(onNavigateToLogin = {
                        navController.navigate(
                            route = Login
                        )
                    })
                }
                composable<Profile> {
                    ProfileScreen(onNavigateToLogin = {
                        navController.navigate(
                            route = Login
                        )
                    })
                }
                composable<Home> { HomeScreen() }
                composable<AddExpense> { AddExpenseScreen() }
            }
        }
    }
}
