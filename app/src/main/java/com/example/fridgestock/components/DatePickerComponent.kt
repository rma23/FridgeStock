package com.example.fridgestock.components

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fridgestock.ui.FoodEntryViewModel
import com.example.fridgestock.ui.FoodEditViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    isShowDatePicker: MutableState<Boolean>,
    dateType: String,
    screenType: String,
) {
    if (screenType == "entry") {
        val viewModel = hiltViewModel<FoodEntryViewModel>()

        if (isShowDatePicker.value) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.lastSelectedExpirationDate ?: Instant.now()
                    .toEpochMilli()
            )
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

            val selectedDate: String? = datePickerState.selectedDateMillis?.let {
                convertMillisToDate(it)
            }

            DatePickerDialog(
                onDismissRequest = { isShowDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (dateType == "purchaseDate") {
                                viewModel.purchaseDate = selectedDate.toString()
                                viewModel.lastSelectedPurchaseDate =
                                    datePickerState.selectedDateMillis
                                isShowDatePicker.value = false
                            } else {
                                viewModel.expirationDate = selectedDate.toString()
                                viewModel.lastSelectedExpirationDate =
                                    datePickerState.selectedDateMillis
                                isShowDatePicker.value = false
                            }
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isShowDatePicker.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    dateFormatter = DatePickerFormatter(
                        selectedDateSkeleton = "yyyy-MM-dd"
                    )
                )
            }
        }
     } else {
        val viewModel = hiltViewModel<FoodEditViewModel>()

        if (isShowDatePicker.value) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.lastSelectedExpirationDate ?: Instant.now()
                    .toEpochMilli()
            )
            val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

            val selectedDate: String? = datePickerState.selectedDateMillis?.let {
                convertMillisToDate(it)
            }

            DatePickerDialog(
                onDismissRequest = { isShowDatePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (dateType == "purchaseDate") {
                                viewModel.purchaseDate = selectedDate.toString()
                                Log.d("${viewModel.purchaseDate}}", "purchaseDateTEST")
                                viewModel.lastSelectedPurchaseDate =
                                    datePickerState.selectedDateMillis
                                isShowDatePicker.value = false
                            } else {
                                viewModel.expirationDate = selectedDate.toString()
                                viewModel.lastSelectedExpirationDate =
                                    datePickerState.selectedDateMillis
                                isShowDatePicker.value = false
                            }
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            isShowDatePicker.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    dateFormatter = DatePickerFormatter(
                        selectedDateSkeleton = "yyyy-MM-dd"
                    )
                )
            }
        }
    }
}

//    Column() {
//        val datePickerState = rememberDatePickerState(
//            initialSelectedDateMillis = null
//        )
//        DatePicker(state = datePickerState, modifier = Modifier.padding(16.dp))
//viewModel: MainViewModel = hiltViewModel(),
//        Text("Entered date timestamp: ${datePickerState.selectedDateMillis ?: "no input"}")
//    }
//}


