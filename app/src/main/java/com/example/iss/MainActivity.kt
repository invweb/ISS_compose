package com.example.iss

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.iss.navigation.Screen
import com.example.iss.ui.AstronautDetailScreen
import com.example.iss.ui.AstroScreen
import com.example.iss.ui.IssScreen
import com.example.iss.ui.MenuScreen
import com.example.iss.ui.SettingsScreen
import com.example.iss.ui.SplashScreen
import com.example.iss.ui.theme.ISSTheme
import com.example.iss.viewmodel.AstroViewModel
import com.example.iss.viewmodel.IssViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISSTheme {
                App()
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @androidx.compose.runtime.Composable
    private fun App() {
        val navController: NavHostController = rememberNavController()
        
        Scaffold(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash
            ) {
                composable<Screen.Splash> {
                    SplashScreen(onFinished = { navController.navigate(Screen.Menu) { popUpTo(Screen.Splash) { inclusive = true } } })
                }

                composable<Screen.Menu> {
                    MenuScreen(
                        onMap = { navController.navigate(Screen.Map) },
                        onSettings = { navController.navigate(Screen.Settings) },
                        onAstro = { navController.navigate(Screen.Astro) },
                        onExit = { finish() }
                    )
                }

                composable<Screen.Map> {
                    val viewModel: IssViewModel = koinViewModel()
                    IssScreen(
                        onBack = { navController.popBackStack() },
                        viewModel = viewModel
                    )
                }

                composable<Screen.Settings> {
                    SettingsScreen(onBack = { navController.popBackStack() })
                }

                composable<Screen.Astro> {
                    val viewModel: AstroViewModel = koinViewModel()
                    AstroScreen(
                        onBack = { navController.popBackStack() },
                        viewModel = viewModel,
                        onPersonClick = { name, craft -> navController.navigate(Screen.Detail(name, craft)) }
                    )
                }

                composable<Screen.Detail> { backStackEntry ->
                    val route = backStackEntry.toRoute<Screen.Detail>()
                    AstronautDetailScreen(
                        name = route.name,
                        craft = route.craft,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
