package com.example.ratocalculator.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.example.ratocalculator.settings.Section.AboutDetailScreen
import com.example.ratocalculator.settings.Section.AboutSection
import com.example.ratocalculator.settings.Section.BasicCalculatorSection
import com.example.ratocalculator.settings.Section.DecimalPlacesScreen
import com.example.ratocalculator.settings.Section.ResetSettingSection
import com.example.ratocalculator.settings.Section.TrailingZeroDetailScreen

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
    ){
    val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)

    var currentSubScreen by remember { mutableStateOf<String?>(null) }
    Box(modifier = Modifier.fillMaxSize()) {

        // If no sub-menu is selected, the main screen will display all sections according to the paper structure.
        if (currentSubScreen == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) }

                // 1: Calculator Section
                item {
                    BasicCalculatorSection(
                        removeTrailingZero = removeTrailingZero,
                        decimalPlaces = decimalPlaces,
                        onNavigateToTrailing = { currentSubScreen = "trailing" },
                        onNavigateToDecimal = { currentSubScreen = "decimal" },
                    )
                }

                // Reset: Reset Section
                item {
                    ResetSettingSection(
                        // Change from viewModel.resetToDefault() or resetCalculatorToDefault
                        // Call the intermediate function we created in ViewModel
                        onResetClick = { viewModel.resetAllToDefault() }
                    )
                }

                // About Section
                item {
                    AboutSection(
                        onNavigateToAbout = { currentSubScreen = "about" }
                    )
                }
            }
        }

        //  User (State Hoisting Navigation)
        if (currentSubScreen == "trailing") {
            TrailingZeroDetailScreen(
                removeTrailingZero = removeTrailingZero,
                onToggleTrailing = { viewModel.updateRemoveTrailingZero(it) },
                onBack = { currentSubScreen = null }
            )
        }

        if (currentSubScreen == "decimal") {
            DecimalPlacesScreen(
                decimalPlaces = decimalPlaces,
                onDecimalChange = { viewModel.updateDecimalPlaces(it) },
                onBack = { currentSubScreen = null }
            )
        }

        if (currentSubScreen == "about") {
            AboutDetailScreen(onBack = { currentSubScreen = null })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        SettingsScreen()
    }
}