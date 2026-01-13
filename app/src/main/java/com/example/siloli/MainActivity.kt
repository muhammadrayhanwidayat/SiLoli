package com.example.siloli

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.siloli.theme.SiloliTheme
import com.example.siloli.view.laporan.*
import com.example.siloli.view.login.LoginScreen
import com.example.siloli.view.register.RegisterScreen
import com.example.siloli.viewmodel.LaporanViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiloliTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ""
                
                val viewModel: LaporanViewModel = hiltViewModel()
                // Use explicit type to avoid compilation error if type inference fails
                val isLoggedInState = viewModel.isLoggedIn.collectAsState()
                val isLoggedIn = isLoggedInState.value

                // Show bottom bar only on main screens
                val showBottomBar = currentRoute in listOf("home", "history", "profile")

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            com.example.siloli.view.components.SiloliBottomNavigation(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        popUpTo("home") {
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
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "home" else "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    navController.popBackStack() 
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("home") {
                            com.example.siloli.view.home.HomeScreen(
                                onNavigateToCreate = { navController.navigate("buat_laporan") },
                                onNavigateToHistory = { 
                                    // Manually replicate BottomNav logic to switch tab
                                    navController.navigate("history") {
                                        popUpTo("home") {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                onNavigateToInfo = { navController.navigate("info") }
                            )
                        }

                        composable("info") {
                            com.example.siloli.view.info.InfoScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("history") {
                            // Reusing LaporanScreen as History
                            LaporanScreen(
                                onNavigateToCreate = {
                                    navController.navigate("buat_laporan")
                                },
                                onNavigateToEdit = { id, wilayah, aduan ->
                                    navController.navigate("edit_laporan/$id/$wilayah/$aduan")
                                },
                                onNavigateToProfile = {
                                    navController.navigate("profile")
                                }
                            )
                        }

                        composable("profile") {
                            com.example.siloli.view.profile.ProfileScreen(
                                onNavigateBack = { 
                                    // If navigating back from profile manually, maybe go home
                                    navController.navigate("home") 
                                },
                                onLogoutSuccess = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true } // Clear entire stack
                                    }
                                }
                            )
                        }

                        composable("buat_laporan") {
                            BuatLaporanScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "edit_laporan/{id}/{wilayah}/{aduan}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType },
                                navArgument("wilayah") { type = NavType.StringType },
                                navArgument("aduan") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 0
                            val wilayah = backStackEntry.arguments?.getString("wilayah") ?: ""
                            val aduan = backStackEntry.arguments?.getString("aduan") ?: ""
                            
                            EditLaporanScreen(
                                id = id,
                                initialWilayah = wilayah,
                                initialAduan = aduan,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
