package com.example.ratocalculator.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
    ){
    Column {
        NavigationDrawerItem(
            label = { Text("Basic Calculator") },
            selected = false,
            onClick = {
                navController.navigate("basic")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text("Scientific Calculator") },
            selected = false,
            onClick = {
                navController.navigate("scientific")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text("Unit Converter") },
            selected = false,
            onClick = {
                navController.navigate("unit_converter")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text("Physics") },
            selected = false,
            onClick = {
                navController.navigate("physics")
                scope.launch {drawerState.close()}
            }
        )
        NavigationDrawerItem(
            label = { Text("Settings") },
            selected = false,
            onClick = {
                navController.navigate("settings")
                scope.launch {drawerState.close()}
            }
        )
    }
}