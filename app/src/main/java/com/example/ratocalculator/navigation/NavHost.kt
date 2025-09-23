package com.example.ratocalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratocalculator.feature.basic.CalculatorScreen
import com.example.ratocalculator.feature.physics.PhysicsScreen
import com.example.ratocalculator.feature.scientific.ScientificCalculatorScreen
import com.example.ratocalculator.feature.unitconverter.UnitConverterScreen
import com.example.ratocalculator.settings.SettingsScreen

@Composable
fun CalculatorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "basic"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("basic") { CalculatorScreen() }
        composable("scientific") { ScientificCalculatorScreen() }
        composable("physics") { PhysicsScreen() }
        composable("unit_converter") { UnitConverterScreen() }
        composable("settings") { SettingsScreen() }
    }
}