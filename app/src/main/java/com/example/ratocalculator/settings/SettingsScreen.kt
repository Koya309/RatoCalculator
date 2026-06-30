package com.example.ratocalculator.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.settings.Section.AboutSection
import com.example.ratocalculator.settings.Section.BasicCalculatorSection
import com.example.ratocalculator.settings.Section.ResetSettingSection

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateToTrailing: () -> Unit,
    onNavigateToDecimal: () -> Unit,
    onNavigateToAbout: () -> Unit

){
    val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)

    // var currentSubScreen by remember { mutableStateOf<String?>(null) }
    Box(modifier = Modifier.fillMaxSize()) {

        // If no sub-menu is selected, the main screen will display all sections according to the paper structure.
    //    if (currentSubScreen == null) {
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
                        onNavigateToTrailing = onNavigateToTrailing,
                        onNavigateToDecimal = onNavigateToDecimal,
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
                        onNavigateToAbout = onNavigateToAbout
                    )
                }
            }
        }

    }

