package com.example.fridgestock

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fridgestock.navigation.ScreenRoute
import com.example.fridgestock.ui.FoodEditScreen
import com.example.fridgestock.ui.FoodEditViewModel
import com.example.fridgestock.ui.FoodEntryScreen
import com.example.fridgestock.ui.FoodEntryViewModel
import com.example.fridgestock.ui.HomeScreen
import com.example.fridgestock.ui.theme.FridgeStockTheme
import com.example.fridgestock.ui.theme.LightFridgeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightFridgeTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainContent() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.HomeScreen.route,
    ) {
        // HomeScreen
        composable(route = ScreenRoute.HomeScreen.route) {
            HomeScreen(navController)
        }
        // FoodEntryScreen
        composable(
            route = ScreenRoute.FoodEntryScreen.route
        ) {
            val viewModel = hiltViewModel<FoodEntryViewModel>()
            FoodEntryScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
        // FoodEditScreen
        composable(
            route = "food_edit_screen/{foodId}",
            arguments = listOf(
                navArgument("foodId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val viewModel = hiltViewModel<FoodEditViewModel>()
            val foodId = backStackEntry.arguments?.getInt("foodId") ?: 0
            viewModel.setId(foodId)
            viewModel.setData()
            FoodEditScreen(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
}
