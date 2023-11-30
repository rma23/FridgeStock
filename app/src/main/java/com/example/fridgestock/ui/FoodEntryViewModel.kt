@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.fridgestock.ui

import android.content.Context
import android.os.Build
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FoodEntryViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {
    var name by mutableStateOf("")
    var amount by mutableStateOf("")
    var purchaseDate by mutableStateOf("")
    var expirationDate by mutableStateOf("")
    var description by mutableStateOf("")
    var addedDate by mutableStateOf("")
    var scheduleAlarm by mutableStateOf(false)

    var lastSelectedPurchaseDate: Long? by mutableStateOf(null)
    var lastSelectedExpirationDate: Long? by mutableStateOf(null)

    val foods = foodDao.loadAllFoods().distinctUntilChanged()

    private val foodId = MutableStateFlow(-1)
    var food = foodId.flatMapLatest { foodId -> foodDao.loadFoodById(foodId) }

    var insertedId = -1L

    @RequiresApi(Build.VERSION_CODES.O)
    fun addFood(context: Context) {
        viewModelScope.launch {
            val newFood = Food(
                name = name,
                amount = amount,
                purchaseDate = purchaseDate,
                expirationDate = expirationDate,
                description = description,
                addedDate = addedDate,
                scheduleAlarm = scheduleAlarm
            )
            insertedId = foodDao.insertFood(newFood)
            if (expirationDate != "" && scheduleAlarm) {
                setAlarm(context)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAlarm(context: Context) {

        val dateTime = convertStringToLocalDateTime(expirationDate)

        // val dateTime = LocalDateTime.parse("2023-11-29T09:25:00.123456", DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val message =
            if (checkExpirationDate((expirationDate))) {
                "${name}の期限は切れています"
            } else {
                "${name}は今日が期限です"
            }

        val alarmScheduler: AlarmScheduler = AlarmSchedulerImpl(context)
        val alarmItem = AlarmItem(
            id = insertedId.toInt(),
            alarmTime = dateTime,
            message = message
        )

        alarmScheduler.schedule(alarmItem)
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

