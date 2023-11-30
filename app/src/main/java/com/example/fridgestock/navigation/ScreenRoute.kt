package com.example.fridgestock.navigation

sealed class ScreenRoute(val route: String) {
    object HomeScreen: ScreenRoute("home_screen")
    object FoodEntryScreen: ScreenRoute("food_entry_screen")
    object FoodDetailScreen: ScreenRoute("food_detail_screen")
    object FoodEditScreen: ScreenRoute("food_edit_screen")
}
