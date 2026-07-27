package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object ServiceList : Screen("service_list", "Servis", Icons.Default.Build)
    object ServiceIntake : Screen("service_intake", "Tambah", Icons.Default.AddCircle)
    object ToolkitAi : Screen("toolkit_ai", "Toolkit AI", Icons.Default.AutoAwesome)
    object Spareparts : Screen("spareparts", "Stok", Icons.Default.Inventory2)
    object Profile : Screen("profile", "Profil", Icons.Default.Store)
    
    // Sub-screens
    object ServiceDetail : Screen("service_detail", "Detail Servis", Icons.Default.Info)
    object InvoicePdf : Screen("invoice_pdf", "Nota Digital", Icons.Default.Receipt)
}
