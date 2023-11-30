@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.fridgestock.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fridgestock.MainViewModel
import com.example.fridgestock.R
import com.example.fridgestock.components.FoodList
import com.example.fridgestock.navigation.ScreenRoute
import com.example.fridgestock.ui.theme.LightFridgeColors

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    BackHandler(
        enabled = viewModel.isInSelectionMode,
    ) {
        viewModel.resetSelectionMode()
    }

    LaunchedEffect(
        key1 = viewModel.isInSelectionMode,
        key2 = viewModel.selectedFoods.size
    ) {
        if (viewModel.isInSelectionMode && viewModel.selectedFoods.isEmpty()) {
            viewModel.isInSelectionMode = false
        }
    }

    if (viewModel.isShowDialog) {
        DeleteSelectedFoodsDialog(
            viewModel = viewModel,
            context = LocalContext.current
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHostState() },
        topBar = {
            if (viewModel.isInSelectionMode) {
                SelectionModeTopAppBar(
                    viewModel = viewModel
                )
            } else {
                HomeScreenTopBar()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ScreenRoute.FoodEntryScreen.route)
                },
                containerColor = LightFridgeColors.pale
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }) {
        Surface(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            val foods by viewModel.foods.collectAsState(initial = emptyList())

            FoodList(
                foods = foods,
                viewModel = viewModel,
                onClickRow = {
                    navController.navigate("food_edit_screen/${it.id}")
                },
            )
        }
    }
}

@Composable
fun HomeScreenTopBar() {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = Black,
            containerColor = LightFridgeColors.pale,
        )
    )
}

@Composable
fun SelectionModeTopAppBar(
    viewModel: MainViewModel
) {
    TopAppBar(
        title = {
            Text(
                text = "${viewModel.selectedFoods.size} 個のアイテムが選択されました",
                fontSize = 20.sp,
            )
        },
        navigationIcon = {
            IconButton(onClick = viewModel.resetSelectionMode) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier,
            ) {
                IconButton(onClick = {
                    viewModel.isShowDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = Black,
            containerColor = LightFridgeColors.pale,
        ),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteSelectedFoodsDialog(
    viewModel: MainViewModel,
    context: Context
) {
    AlertDialog(
        onDismissRequest = { viewModel.isShowDialog = false },
        title = { Text(stringResource(R.string.confirm_delete_selected_foods)) },
        confirmButton = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        viewModel.isShowDialog = false
                    },
                ) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    modifier = Modifier.width(120.dp),
                    onClick = {
                        viewModel.selectedFoods.forEach {
                            viewModel.deleteFood(it, context)
                        }
                        viewModel.resetSelectionMode()
                        viewModel.isShowDialog = false
                    },
                ) {
                    Text(text = "OK")
                }
            }
        },
    )
}

