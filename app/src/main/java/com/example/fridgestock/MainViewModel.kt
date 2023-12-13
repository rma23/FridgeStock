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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(private val foodDao: FoodDao) : ViewModel() {

    var isInSelectionMode by mutableStateOf(false)
    var selectedFoods = mutableStateListOf<Food>()
    val resetSelectionMode = {
        isInSelectionMode = false
        selectedFoods.clear()
    }

    var isShowDeleteDialog by mutableStateOf(false)
    var isShowSortDialog by mutableStateOf(false)

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

    val radioOptions = listOf("登録日：古 → 新", "登録日：新 → 古", "購入日：古 → 新", "購入日：新 → 古", "期限：古 → 新", "期限：新 → 古")
    var selectedOrder by mutableStateOf(radioOptions[0])
    var initialSelectedOrder = selectedOrder

    private val addedDateComparator = Comparator<Food> { left, right ->
        left.id.compareTo(right.id)
    }

    private val reverseAddedDateComparator = Comparator<Food> { left, right ->
        right.id.compareTo(left.id)
    }

    private val purchaseDateComparator = Comparator<Food> { left, right ->
        left.purchaseDate.compareTo(right.purchaseDate)
    }

    private val reversePurchaseDateComparator = Comparator<Food> { left, right ->
        right.purchaseDate.compareTo(left.purchaseDate)
    }

    private val expirationDateComparator = Comparator<Food> { left, right ->
        left.expirationDate.compareTo(right.expirationDate)
    }

    private val reverseExpirationDateComparator = Comparator<Food> { left, right ->
        right.expirationDate.compareTo(left.expirationDate)
    }

    fun sortFoods(
        foods: List<Food>,
    ) : List<Food> {
        return when (selectedOrder) {
            radioOptions[1] -> {
                foods.sortedWith(reverseAddedDateComparator)
            }
            radioOptions[2] -> {
                foods.sortedWith(purchaseDateComparator)
            }
            radioOptions[3] -> {
                foods.sortedWith(reversePurchaseDateComparator)
            }
            radioOptions[4] -> {
                foods.sortedWith(expirationDateComparator)
            }
            radioOptions[5] -> {
                foods.sortedWith(reverseExpirationDateComparator)
            }
            else -> {
                foods.sortedWith(addedDateComparator)
            }
        }
    }

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
