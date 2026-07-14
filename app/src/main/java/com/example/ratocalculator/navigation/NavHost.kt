package com.example.ratocalculator.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ratocalculator.feature.basic.CalculatorScreen
import com.example.ratocalculator.feature.physics.PhysicsScreen
import com.example.ratocalculator.feature.scientific.ScientificCalculatorScreen
import com.example.ratocalculator.feature.unitconverter.UnitConverterScreen
import com.example.ratocalculator.settings.section.AboutDetailScreen
import com.example.ratocalculator.settings.section.DecimalPlacesScreen
import com.example.ratocalculator.settings.section.TrailingZeroDetailScreen
import com.example.ratocalculator.settings.SettingsScreen
import com.example.ratocalculator.settings.SettingsViewModel
import com.example.ratocalculator.settings.section.AdvancedPrefixScreen
import com.example.ratocalculator.settings.section.SideBlockScreen

@Composable
fun CalculatorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "basic",
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Main NavHost Calculator
        composable("basic") { CalculatorScreen() }
        composable("scientific") { ScientificCalculatorScreen() }
        composable("physics") { PhysicsScreen() }
        composable("unit_converter") { UnitConverterScreen() }

        // 2. Main Screen Settings
        composable("settings") {
            SettingsScreen(
                onNavigateToTrailing = { navController.navigate("trailing") },
                onNavigateToDecimal = { navController.navigate("decimal") },
                onNavigateToAbout = { navController.navigate("about") },
                onNavigateToSideBlock = {navController.navigate("sideblock")},
                onNavigateToAdvancedPrefix = {navController.navigate("advancedprefix")}
            )
        }

        // Screen Trailing
        composable("trailing") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)

            TrailingZeroDetailScreen(
                removeTrailingZero = removeTrailingZero,
                onToggleTrailing = { viewModel.updateRemoveTrailingZero(it) }
            )
        }

        // Screen Decimal
        composable("decimal") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)

            DecimalPlacesScreen(
                decimalPlaces = decimalPlaces,
                onDecimalChange = { viewModel.updateDecimalPlaces(it) }
            )
        }

        composable("sideblock"){
            val viewModel: SettingsViewModel = hiltViewModel()
            val sideBlock by viewModel.sideBlock.collectAsState(initial = true)

            SideBlockScreen(
                sideBlock = sideBlock
            )
        }

        composable("advancedprefix") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val AdvancedPrefix by viewModel.AdvancedPrefixEnabled.collectAsState(initial = false)

            AdvancedPrefixScreen(
            //    AdvancedPrefix = AdvancedPrefix
            )
        }

        // About screen
        composable("about") {
            AboutDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}