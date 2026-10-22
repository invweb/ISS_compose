package com.example.iss.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.iss.ui.AstronautDetailScreen
import com.example.iss.ui.AstroScreen
import com.example.iss.ui.IssScreen
import com.example.iss.ui.MenuScreen
import com.example.iss.ui.SettingsScreen
import com.example.iss.ui.SplashScreen
import com.example.iss.viewmodel.AstroViewModel
import com.example.iss.viewmodel.IssViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Any = Screen.Splash,
    issViewModel: IssViewModel = viewModel(),
    astroViewModel: AstroViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash is handled separately with LaunchedEffect
        composable<Screen.Splash> {
            SplashScreen(onFinished = { navController.navigate(Screen.Menu) { popUpTo(Screen.Splash) { inclusive = true } } })
        }

        composable<Screen.Menu> {
            MenuScreen(
                onMap = { navController.navigate(Screen.Map) },
                onSettings = { navController.navigate(Screen.Settings) },
                onAstro = { navController.navigate(Screen.Astro) },
                onExit = { /* handled by MainActivity */ }
            )
        }

        composable<Screen.Map> {
            IssScreen(onBack = { navController.popBackStack() })
        }

        composable<Screen.Settings> {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable<Screen.Astro> {
            AstroScreen(
                onBack = { navController.popBackStack() },
                onPersonClick = { name, craft -> navController.navigate(Screen.Detail(name, craft)) }
            )
        }

        composable<Screen.Detail> { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val craft = backStackEntry.arguments?.getString("craft") ?: ""
            AstronautDetailScreen(
                name = name,
                craft = craft,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
