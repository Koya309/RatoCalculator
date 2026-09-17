package com.example.ratocalculator.settings

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.core.utils.LockScreenOrientation
import com.example.ratocalculator.feature.unitconverter.UnitDisplayMode
import com.example.ratocalculator.settings.model.AppLanguage
import com.example.ratocalculator.settings.model.AppTheme
import com.example.ratocalculator.settings.section.AboutSection
import com.example.ratocalculator.settings.section.BasicCalculatorSection
import com.example.ratocalculator.settings.section.ResetSettingSection
import com.example.ratocalculator.settings.section.SystemSection
import com.example.ratocalculator.settings.section.UnitConverterSection

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateToTrailing: () -> Unit,
    onNavigateToDecimal: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToSideBlock: () -> Unit,
    onNavigateToAdvancedPrefix: () -> Unit,
    onNavigateToUnitDisplayMode: () -> Unit,
    onNavigateToTheme:() -> Unit,
    onNavigateToLanguage:() -> Unit,
    onNavigateResetToDefault: () -> Unit

) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)
    val sideBlock by viewModel.sideBlock.collectAsState(initial = true)
    val AdvancedPrefix by viewModel.AdvancedPrefix.collectAsState(initial = true)
    val unitDisplayMode by viewModel.unitDisplayMode.collectAsState(initial = UnitDisplayMode.FULL_NAME)
    val currentTheme by viewModel.appTheme.collectAsState(initial = AppTheme.SYSTEM)
    val currentLanguage by viewModel.appLanguage.collectAsState(initial = AppLanguage.SYSTEM)

    // If no sub-menu is selected, the main screen will display all sections according to the paper structure.
    // if (currentSubScreen == null) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                    )
                }

            // 1: Calculator Section
            item {
                BasicCalculatorSection(
                    removeTrailingZero = removeTrailingZero,
                    decimalPlaces = decimalPlaces,
                    onNavigateToTrailing = onNavigateToTrailing,
                    onNavigateToDecimal = onNavigateToDecimal,
                )
            }

            //2: Unit Converter
            item {
                UnitConverterSection(
                    sideBlock = sideBlock,
                    AdvancedPrefix = AdvancedPrefix,
                    unitDisplayMode = unitDisplayMode,
                    onNavigateToSideBlock = onNavigateToSideBlock,
                    onNavigateToAdvancedPrefix = onNavigateToAdvancedPrefix,
                    onNavigateToUnitDisplayMode = onNavigateToUnitDisplayMode
                )
            }

            // System
            item {
                SystemSection( //Theme+Language
                    currentTheme = currentTheme,
                    currentLanguage = currentLanguage,
                    onNavigateToTheme = onNavigateToTheme,
                    onNavigateToLanguage = onNavigateToLanguage
                )
            }

            // Reset: Reset Section
        item {
            ResetSettingSection(
                onNavigateResetToDefault = onNavigateResetToDefault
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

