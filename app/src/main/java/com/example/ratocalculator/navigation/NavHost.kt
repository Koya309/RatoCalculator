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
import com.example.ratocalculator.settings.Section.AboutDetailScreen
import com.example.ratocalculator.settings.Section.DecimalPlacesScreen
import com.example.ratocalculator.settings.Section.TrailingZeroDetailScreen
import com.example.ratocalculator.settings.SettingsScreen
import com.example.ratocalculator.settings.SettingsViewModel

@Composable
fun CalculatorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = "basic",
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. หน้าจอเครื่องคิดเลขต่างๆ
        composable("basic") { CalculatorScreen() }
        composable("scientific") { ScientificCalculatorScreen() }
        composable("physics") { PhysicsScreen() }
        composable("unit_converter") { UnitConverterScreen() }

        // 2. หน้าหลักของ Settings (ใส่ Lambda สำหรับการเปลี่ยนหน้าย่อยตรงนี้)
        composable("settings") {
            SettingsScreen(
                onNavigateToTrailing = { navController.navigate("trailing") },
                onNavigateToDecimal = { navController.navigate("decimal") },
                onNavigateToAbout = { navController.navigate("about") }
            )
        }

        // 3. หน้าจอรายละเอียดหน่วยย่อย (ถอนตัวออกจาก Box/State มาเป็นหน้าเดี่ยวใน Navigation)
        composable("trailing") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)

            TrailingZeroDetailScreen(
                removeTrailingZero = removeTrailingZero,
                onToggleTrailing = { viewModel.updateRemoveTrailingZero(it) },
                onBack = { navController.popBackStack() } // พาย้อนกลับไปหน้า Settings หลัก
            )
        }

        // หน้าจอ Decimal ที่แยกไว้ก็นำมาใส่ตรงนี้
        composable("decimal") {
            val viewModel: SettingsViewModel = hiltViewModel()
            val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)

            DecimalPlacesScreen(
                decimalPlaces = decimalPlaces,
                onDecimalChange = { viewModel.updateDecimalPlaces(it) },
                onBack = { navController.popBackStack() }
            )
        }

        // หน้าจอ About ที่แยกไว้ก็นำมาใส่ตรงนี้
        composable("about") {
            AboutDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}