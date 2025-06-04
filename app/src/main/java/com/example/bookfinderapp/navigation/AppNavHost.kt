package com.example.bookfinderapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookfinderapp.ui.screen.home.HomeScreen
import com.example.bookfinderapp.ui.screen.login.LoginScreen
import com.example.bookfinderapp.ui.screen.register.RegisterScreen
import com.example.bookfinderapp.ui.screen.search.SearchScreen
import com.example.bookfinderapp.ui.screen.details.BookDetailsScreen
import com.example.bookfinderapp.ui.screen.library.LibraryScreen
import com.example.bookfinderapp.ui.screen.settings.SettingsScreen
import com.example.bookfinderapp.data.model.Book
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Settings

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object BookDetails : Screen("bookDetails/{bookId}", "Book Details") {
        fun createRoute(bookId: String) = "bookDetails/$bookId"
    }
    object Library : Screen("library", "Library", Icons.Default.ThumbUp)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavigationItems = listOf(
    Screen.Home,
    Screen.Search,
    Screen.Library,
    Screen.Settings
)

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Check if the current route is one of the authentication screens
    val isAuthScreen = currentRoute == Screen.Login.route || currentRoute == Screen.Register.route

    Scaffold(
        bottomBar = {
            if (!isAuthScreen) {
                NavigationBar {
                    bottomNavigationItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                            label = { Text(text = screen.title) },
                            selected = currentRoute?.startsWith(screen.route) == true,
                            onClick = {
                                // Avoid recreating the same screen
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination when
                                        // reselecting the same item
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
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
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }},
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }},
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetails.createRoute(book.id))
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetails.createRoute(book.id))
                    }
                )
            }
            composable(Screen.BookDetails.route) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                BookDetailsScreen(
                    bookId = bookId
                )
            }
            composable(Screen.Library.route) {
                LibraryScreen(
                    onBookClick = { book ->
                        navController.navigate(Screen.BookDetails.createRoute(book.id))
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onLogoutSuccess = {
                        navController.navigate(Screen.Login.route) {
                            // Clear the entire back stack so the user can't navigate back to protected screens
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}
