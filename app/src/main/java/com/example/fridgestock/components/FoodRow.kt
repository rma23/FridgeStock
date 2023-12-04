package com.example.fridgestock.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fridgestock.MainViewModel
import com.example.fridgestock.R
import com.example.fridgestock.data.Food
import com.example.fridgestock.ui.theme.LightFridgeColors

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodRow(
    food: Food,
    viewModel: MainViewModel,
    isSelected: Boolean,
    onClickRow: (Food) -> Unit,
) {
    val date = food.expirationDate

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .size(width = 240.dp, height = 90.dp)
            .combinedClickable(
                onClick = {
                    if (viewModel.isInSelectionMode) {
                        if (isSelected) {
                            viewModel.selectedFoods.remove(food)
                        } else {
                            viewModel.selectedFoods.add(food)
                        }
                    } else {
                        onClickRow(food)
                    }
                },
                onLongClick = {
                    if (viewModel.isInSelectionMode) {
                        if (isSelected) {
                            viewModel.selectedFoods.remove(food)
                        } else {
                            viewModel.selectedFoods.add(food)
                        }
                    } else {
                        viewModel.isInSelectionMode = true
                        viewModel.selectedFoods.add(food)
                    }
                }
            ),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (isSelected) Color.LightGray
            else if (checkExpirationDate(date)) LightFridgeColors.alarm
            else LightFridgeColors.veryPale
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            // horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = food.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Row {
                Text(text = stringResource(id = R.string.food_amount))
                Text(text = ": ${food.amount}")
            }
            Row {
                Text(text = stringResource(id = R.string.food_expiration_date))
                val expirationDateReplaced = food.expirationDate
                    .replace(Regex("[年月]"), "/").replace(Regex("日"), "")
                Text(text = ": $expirationDateReplaced")
                if (expirationDateReplaced != "" && food.scheduleAlarm) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .padding(2.dp)
                            .size(32.dp)
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun FoodRowPreview() {
//    FoodRow(
//        food = Food(name = "testFood", amount = "5", purchaseDate = "", expirationDate = "2023-04-01", description = "", addedDate = "", scheduleAlarm = false),
//        viewModel = hiltViewModel(),
//        isSelected = false,
//        onClickRow = {},
//    )
//}