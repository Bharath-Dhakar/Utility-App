package com.example.utilityapp.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.utilityapp.ui.theme.Screens.CompassScreen
import com.example.utilityapp.ui.theme.Screens.ShakeScreen
import com.example.utilityapp.ui.theme.Screens.StepScreen
import com.example.utilityapp.ui.theme.components.BottomNavItem
import com.example.utilityapp.ui.theme.components.CustomBottomNavBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(BottomNavItem.Compass) }


    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        // NavHost for screen navigation
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Compass.route
            ) {
                composable(BottomNavItem.Shake.route) {
                    ShakeScreen()
                }
                composable(BottomNavItem.Compass.route) {
                    CompassScreen()
                }
                composable(BottomNavItem.Steps.route) {
                    StepScreen()
                }
            }
        }

        // Custom Bottom Navigation Bar
        CustomBottomNavBar(
            selectedTab = selectedTab,
            onTabSelected = { item ->
                selectedTab = item
                navController.navigate(item.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
