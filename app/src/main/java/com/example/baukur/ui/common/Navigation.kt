package com.example.baukur.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.baukur.ui.screens.EditProfileScreen
import com.example.baukur.ui.screens.HomeScreen
import com.example.baukur.ui.screens.LoginScreen
import com.example.baukur.ui.screens.ProfileScreen
import com.example.baukur.ui.screens.RegisterScreen
import com.example.baukur.ui.screens.categories.AddCategoryScreen
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
@Serializable
object EditProfile
@Serializable
object AddCategory

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Home", Home, Icons.Default.Home),
    TopLevelRoute("Expenses", AddExpense, Icons.Default.AddCircle),
    TopLevelRoute("Profile", Profile, Icons.Default.Person),
)

val hideNavBarRoutes = listOf(
    Login,
    Register,
)

val nestedRoutes = listOf(
    EditProfile,
    AddCategory,
)

/*
Essentially the entry-point for the app,
enforces an always-present scaffold and (sometimes present) navigation bar
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    BaukurTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFFAF3F3),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val showBackArrow = nestedRoutes.any {
                    currentDestination?.hasRoute(it::class) == true
                }
                if (showBackArrow) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                content = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                },
                                onClick = { navController.popBackStack() }
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFFAF3F3),
                        ),
                        title = { }
                    )
                }
            },
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val hideNavigationBar = hideNavBarRoutes.any {
                    currentDestination?.hasRoute(it::class) == true
                }
                if (hideNavigationBar.not()) {
                    NavigationBar {
                        topLevelRoutes.forEach { route ->
                            NavigationBarItem(
                                icon = { Icon(route.icon, route.name) },
                                label = { Text(route.name) },
                                selected = currentDestination?.hierarchy?.any {
                                    it.hasRoute(
                                        route.route::class
                                    )
                                } == true,
                                onClick = {
                                    navController.navigate(route.route) {
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
                        onNavigateToHome = { navController.navigate(route = Home) },
                        snackbarHostState = snackbarHostState
                    )
                }
                composable<Register> {
                    RegisterScreen(onNavigateToLogin = {
                        navController.navigate(
                            route = Login
                        )
                    },
                    snackbarHostState = snackbarHostState
                    )
                }
                composable<Profile> {
                    ProfileScreen(
                        onNavigateToLogin = {
                            navController.navigate(
                                route = Login
                            )
                        },
                        onNavigateToEditProfile = {
                            navController.navigate(
                                route = EditProfile
                            )
                        }
                    )
                }

                composable<EditProfile> {
                    EditProfileScreen(
                        onSaveProfile = {
                            navController.navigate(
                                route = Login
                            )
                        },
                        onCancel = {
                            navController.navigate(
                                route = Profile
                            )
                        }
                    )
                }


                composable<Home> { HomeScreen() }
                composable<AddCategory> {
                    AddCategoryScreen(
                        snackbarHostState = snackbarHostState,
                        onNavigateToCreateExpense = {
                            navController.navigate(route = AddExpense)
                        }
                    )
                }
                composable<AddExpense> {
                    AddExpenseScreen(
                        snackbarHostState = snackbarHostState,
                        navigateToNewCategory = {
                            navController.navigate(route = AddCategory)
                        }
                    )
                }
            }
        }
    }
}
