package com.example.fridgestock.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.example.fridgestock.MainViewModel
import com.example.fridgestock.data.Food

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodList(
    foods: List<Food>,
    viewModel: MainViewModel,
    onClickRow: (Food) -> Unit,
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(foods) { food ->
            val isSelected = viewModel.selectedFoods.contains(food)
            FoodRow(
                food = food,
                viewModel = viewModel,
                isSelected = isSelected,
                onClickRow = onClickRow,
            )
        }
    }
}