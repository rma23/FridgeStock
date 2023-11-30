@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.example.fridgestock

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fridgestock.alarm.AlarmItem
import com.example.fridgestock.alarm.AlarmScheduler
import com.example.fridgestock.alarm.AlarmSchedulerImpl
import com.example.fridgestock.components.convertStringToLocalDateTime
import com.example.fridgestock.data.Food
import com.example.fridgestock.data.FoodDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {

    var isInSelectionMode by mutableStateOf(false)
    var selectedFoods = mutableStateListOf<Food>()
    val resetSelectionMode = {
        isInSelectionMode = false
        selectedFoods.clear()
    }

    var isShowDialog by mutableStateOf(false)//        actions = {
//            IconButton(onClick = { update() }) {
//                Icon(Icons.Default.Edit, "Update")
//            }
//        },


    var name by mutableStateOf("")
    var amount by mutableStateOf("")
    var purchaseDate by mutableStateOf("")
    var expirationDate by mutableStateOf("")
    var description by mutableStateOf("")
    var addedDate by mutableStateOf("")
    var scheduleAlarm by mutableStateOf(false)

    val foods = foodDao.loadAllFoods().distinctUntilChanged()

    private val foodId = MutableStateFlow(-1)
    var food = foodId.flatMapLatest { foodId -> foodDao.loadFoodById(foodId) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteFood(
        food: Food,
        context: Context
    ) {
        viewModelScope.launch {
            // 削除するアイテムにアラームがセットされていたらキャンセルする
            if (food.expirationDate != "" && food.scheduleAlarm) {
                unsetAlarm(food, context)
            }
            foodDao.deleteFood(food)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun unsetAlarm(
        food: Food,
        context: Context
    ) {
        val dateTime = convertStringToLocalDateTime(food.expirationDate)
        val message = ""

        val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)
        val alarmItem = AlarmItem(
            id = food.id,
            alarmTime = dateTime,
            message = message
        )

        alarmScheduler.cancel(alarmItem)
    }
}

private const val emptyFoodId = -1
private val emptyFood = Food(
    id = emptyFoodId,
    name = "",
    amount = "",
    purchaseDate = "",
    expirationDate = "",
    description = "",
    addedDate = "",
    scheduleAlarm = false
)
