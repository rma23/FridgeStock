@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fridgestock.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fridgestock.R
import com.example.fridgestock.components.DatePickerComponent
import com.example.fridgestock.data.Food
import com.example.fridgestock.navigation.ScreenRoute
import com.example.fridgestock.ui.theme.LightFridgeColors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodEditScreen (
    navController: NavController,
    viewModel: FoodEditViewModel,
) {
    var food = viewModel.food.collectAsState(emptyFood)

    val context = LocalContext.current

    Scaffold(
        topBar = {
            FoodEditTopBar(navController)
        },
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            FoodEditBody(
                navController,
                viewModel,
                context,
            ) {
                navController.navigate(ScreenRoute.HomeScreen.route)
                viewModel.delete(food.value, context)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEditTopBar(
    navController: NavController,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Black,
                )
            }
        },
        title = { Text(text = stringResource(id = R.string.detail)) },
//        actions = {
//            IconButton(onClick = { update() }) {
//                Icon(Icons.Default.Edit, "Update")
//            }
//        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = Black,
            containerColor = LightFridgeColors.pale
        ),
    )
}


@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodEditBody(
    navController: NavController,
    viewModel: FoodEditViewModel,
    context: Context,
    delete: () -> Unit,
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        val isShowDatePicker = remember { mutableStateOf(false) }
        val dateType = remember { mutableStateOf("") }

        val name = remember(key1 = viewModel.editedFood.name) { mutableStateOf(viewModel.editedFood.name) }
        val amount = remember(key1 = viewModel.editedFood.amount) { mutableStateOf(viewModel.editedFood.amount) }
        val purchaseDate = remember(key1 = viewModel.editedFood.purchaseDate) { mutableStateOf(viewModel.editedFood.purchaseDate) }
        val expirationDate = remember(key1 = viewModel.editedFood.expirationDate) { mutableStateOf(viewModel.editedFood.expirationDate) }
        val description = remember(key1 = viewModel.editedFood.description) { mutableStateOf(viewModel.editedFood.description) }
        val scheduleAlarm = remember(key1 = viewModel.editedFood.scheduleAlarm) { mutableStateOf(viewModel.editedFood.scheduleAlarm) }
        val addedDate = viewModel.editedFood.addedDate

        if (isShowDatePicker.value) {
            DatePickerComponent(
                isShowDatePicker = isShowDatePicker,
                dateType = dateType.value,
                screenType = "edit",
            )
        }

        if (viewModel.purchaseDate != "") {
            purchaseDate.value = viewModel.purchaseDate
            viewModel.purchaseDate = ""
        }

        if (viewModel.expirationDate != "") {
            expirationDate.value = viewModel.expirationDate
            viewModel.expirationDate = ""
        }

        if (viewModel.isShowDialog) {
            AlertDialog(
                onDismissRequest = {
                viewModel.isShowDialog = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            delete()
                            viewModel.isShowDialog = false
                        },
                    ) {
                        Text(text = stringResource(id = R.string.delete))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            viewModel.isShowDialog = false
                        },
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.delete_message))
                },
                text = {

                }
            )
        }

        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = stringResource(id = R.string.food_name))
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.food_amount))
            OutlinedTextField(
                value = amount.value,
                onValueChange = { amount.value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.food_purchase_date))
            Row() {
                OutlinedTextField(
                    value = purchaseDate.value,
                    onValueChange = { purchaseDate.value = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier
                        .width(200.dp)
                        .clickable(onClick = {
                            dateType.value = "purchaseDate"
                            isShowDatePicker.value = true
                        }),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.DarkGray,
                        disabledBorderColor = Color.Gray,
                    ),
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {
                        purchaseDate.value = ""
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = R.string.food_expiration_date))
            Row() {
                OutlinedTextField(
                    value = expirationDate.value,
                    onValueChange = { expirationDate.value = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier
                        .width(200.dp)
                        .clickable(onClick = {
                            dateType.value = "expirationDate"
                            isShowDatePicker.value = true
                        }),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.DarkGray,
                        disabledBorderColor = Color.Gray,
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {
                        expirationDate.value = ""
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(24.dp))
                }
            }
            Text(text = stringResource(id = R.string.food_description))
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = stringResource(R.string.schedule_alarm))
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = scheduleAlarm.value,
                    onCheckedChange = { scheduleAlarm.value = it },
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.update(
                            name.value,
                            amount.value,
                            purchaseDate.value,
                            expirationDate.value,
                            description.value,
                            scheduleAlarm.value,
                            addedDate,
                            context
                        )
                        navController.popBackStack()
                    },
                ) {
                    Text(text = stringResource(id = R.string.update))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        viewModel.isShowDialog = true
                    },
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }
        }
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
