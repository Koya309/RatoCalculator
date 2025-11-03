package com.example.ratocalculator.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.ratocalculator.R

@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
    ){
    Column {
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.basic_calculator))},
            selected = false,
            onClick = {
                navController.navigate("basic")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.scientific_calculator))},
            selected = false,
            onClick = {
                navController.navigate("scientific")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.unit_converter))},
            selected = false,
            onClick = {
                navController.navigate("unit_converter")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.physics_calculator))},
            selected = false,
            onClick = {
                navController.navigate("physics")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.settings))},
            selected = false,
            onClick = {
                navController.navigate("settings")
                scope.launch {drawerState.close()}
            }
        )
    }
}