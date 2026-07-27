package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.NeoBrutalistBottomNavBar
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.GankColors
import com.example.ui.theme.GankTeknisiTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GankTeknisiTheme {
                GankTeknisiApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GankTeknisiApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

    val hideBottomBarRoutes = listOf(
        Screen.ServiceIntake.route,
        Screen.ServiceDetail.route,
        Screen.InvoicePdf.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in hideBottomBarRoutes) {
                NeoBrutalistBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = GankColors.Paper
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route
            ) {
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToNewIntake = { navController.navigate(Screen.ServiceIntake.route) },
                        onNavigateToDetail = { ticket ->
                            viewModel.selectTicket(ticket)
                            navController.navigate(Screen.ServiceDetail.route)
                        },
                        onNavigateToAi = { navController.navigate(Screen.ToolkitAi.route) },
                        onNavigateToSpareparts = { navController.navigate(Screen.Spareparts.route) }
                    )
                }

                composable(Screen.ServiceList.route) {
                    ServiceListScreen(
                        viewModel = viewModel,
                        onNavigateToDetail = { ticket ->
                            viewModel.selectTicket(ticket)
                            navController.navigate(Screen.ServiceDetail.route)
                        },
                        onNavigateToNewIntake = { navController.navigate(Screen.ServiceIntake.route) }
                    )
                }

                composable(Screen.ServiceIntake.route) {
                    ServiceIntakeScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onSuccessSaved = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(Screen.ServiceDetail.route) {
                    ServiceDetailScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNavigateToInvoice = { ticket ->
                            viewModel.selectTicket(ticket)
                            navController.navigate(Screen.InvoicePdf.route)
                        }
                    )
                }

                composable(Screen.InvoicePdf.route) {
                    InvoicePdfScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(Screen.ToolkitAi.route) {
                    ToolkitAiScreen(viewModel = viewModel)
                }

                composable(Screen.Spareparts.route) {
                    SparepartScreen(viewModel = viewModel)
                }

                composable(Screen.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}
