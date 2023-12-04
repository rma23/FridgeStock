@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.example.fridgestock.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.fridgestock.R
import com.example.fridgestock.components.DatePickerComponent
import com.example.fridgestock.components.convertMillisToDate
import com.example.fridgestock.ui.theme.LightFridgeColors
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodEntryScreen(
    navController: NavController,
    viewModel: FoodEntryViewModel,
) {
    Scaffold(
        snackbarHost = { SnackbarHostState() },
        topBar = {
            FoodEntryTopBar(navController)
        },
    ) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            FoodEntryBody(
                navController = navController,
                viewModel = viewModel,
            )
        }
    }
}

@Composable
fun FoodEntryTopBar(
    navController: NavController
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.entry)) },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = Black,
            containerColor = LightFridgeColors.pale,
        ),
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
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodEntryBody(
    navController: NavController,
    viewModel: FoodEntryViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {

        val isShowDatePicker = remember { mutableStateOf(false) }
        val dateType = remember { mutableStateOf("") }

        val context = LocalContext.current

        if (isShowDatePicker.value) {
            DatePickerComponent(
                isShowDatePicker = isShowDatePicker,
                dateType = dateType.value,
                screenType = "entry"
            )
        }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()) { }

        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(text = stringResource(id = R.string.food_name))
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
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
                value = viewModel.amount,
                onValueChange = { viewModel.amount = it },
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
                    value = viewModel.purchaseDate,
                    onValueChange = { viewModel.purchaseDate = it },
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
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = {
                        viewModel.purchaseDate = ""
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = R.string.food_expiration_date))
            Row() {
                OutlinedTextField(
                    value = viewModel.expirationDate,
                    onValueChange = { viewModel.expirationDate = it },
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
                        viewModel.expirationDate = ""
                    }
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.food_description))
            OutlinedTextField(
                value = viewModel.description,
                onValueChange = { viewModel.description = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.clearFocus()
                }),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
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
                    checked = viewModel.scheduleAlarm,
                    onCheckedChange = { viewModel.scheduleAlarm = it },
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                        viewModel.addedDate = convertMillisToDate(Instant.now().toEpochMilli())
                        viewModel.addFood(context)
                        navController.popBackStack()
                    },
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}