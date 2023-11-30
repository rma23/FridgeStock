@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.fridgestock.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fridgestock.alarm.AlarmItem
import com.example.fridgestock.alarm.AlarmScheduler
import com.example.fridgestock.alarm.AlarmSchedulerImpl
import com.example.fridgestock.components.checkExpirationDate
import com.example.fridgestock.components.convertStringToLocalDateTime
import com.example.fridgestock.data.Food
import com.example.fridgestock.data.FoodDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FoodEditViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {

    private val foodId = MutableStateFlow(-1)
    val food: Flow<Food> = foodId.flatMapLatest { foodId -> foodDao.loadFoodById(foodId) }
    var editedFood = emptyFood

    var name by mutableStateOf("")
    var amount by mutableStateOf("")
    var purchaseDate by mutableStateOf("")
    var expirationDate by mutableStateOf("")
    var description by mutableStateOf("")
    var addedDate by mutableStateOf("")
    var scheduleAlarm by mutableStateOf(false)

    var lastSelectedPurchaseDate: Long? by mutableStateOf(null)
    var lastSelectedExpirationDate: Long? by mutableStateOf(null)

    var isShowDialog by mutableStateOf(false)

    fun setId(selectedFoodId: Int) {
        foodId.value = selectedFoodId
    }

    fun setData() {
        viewModelScope.launch {
            food.collect {
                editedFood = it?: emptyFood
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(
        name: String,
        amount: String,
        purchaseDate: String,
        expirationDate: String,
        description: String,
        scheduleAlarm: Boolean,
        addedDate: String,
        context: Context
    ) {
        viewModelScope.launch {
            val updatedFood = Food(
                id = editedFood.id,
                name = name,
                amount = amount,
                purchaseDate = purchaseDate,
                expirationDate = expirationDate,
                description = description,
                scheduleAlarm = scheduleAlarm,
                addedDate = addedDate,
            )
            foodDao.updateFood((updatedFood))
            if (expirationDate != "" && scheduleAlarm) {
                setAlarm(name, expirationDate, context)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAlarm(
        name: String,
        expirationDate: String,
        context: Context
    ) {

        val dateTime = convertStringToLocalDateTime(expirationDate)
        // val dateTime = LocalDateTime.parse("2023-11-29T09:25:00.123456", DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val message =
            if (checkExpirationDate(expirationDate)) {
                "${name}の期限は切れています"
            } else {
                "${name}は今日が期限です"
            }

        val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)
        val alarmItem = AlarmItem(
            id = editedFood.id,
            alarmTime = dateTime,
            message = message
        )

        alarmScheduler.schedule(alarmItem)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun delete(
        food: Food,
        context: Context
    ) {
        viewModelScope.launch {
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
        Log.d("cancelTEST", "cancel in ViewModel")
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
