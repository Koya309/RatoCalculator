package com.example.ratocalculator.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratocalculator.feature.unitconverter.UnitDisplayMode
import com.example.ratocalculator.settings.repository.CalculatorRepository
import com.example.ratocalculator.settings.repository.UnitConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val calculatorRepo: CalculatorRepository,
    private val unitConverterRepo: UnitConverterRepository
//    private val languageRepo: LanguageRepository,
//    private val themeRepo: ThemeRepository
) : ViewModel() {

    // ─── 1.Basic Calculator ───
    val decimalPlaces = calculatorRepo.decimalPlaces
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val removeTrailingZero = calculatorRepo.removeTrailingZero
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun updateDecimalPlaces(places: Int) {
        viewModelScope.launch { calculatorRepo.saveDecimalPlaces(places) }
    }

    fun updateRemoveTrailingZero(value: Boolean) {
        viewModelScope.launch { calculatorRepo.saveRemoveTrailingZero(value) }
    }

    fun resetCalculatorToDefault() {
        viewModelScope.launch { calculatorRepo.resetToDefault() }
    }

    fun resetAllToDefault() {
        viewModelScope.launch {
            // 1. Reset Calculator
            calculatorRepo.resetToDefault()

        }
    }

    // ─── 2.Unit Converter ───
    val sideBlock = unitConverterRepo.sideBlock
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val AdvancedPrefix = unitConverterRepo.AdvancedPrefix
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true )

    val unitDisplayMode = unitConverterRepo.unitDisplayMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UnitDisplayMode.FULL_NAME)

    fun updateSideBlock(value: Boolean) {
        viewModelScope.launch {
            unitConverterRepo.saveSideBlock(value)
        }
    }

    fun updateAdvancedPrefix(value: Boolean) {
        viewModelScope.launch {
            unitConverterRepo.saveAdvancedPrefix(value)
        }
    }

    fun updateUnitDisplayMode(mode: UnitDisplayMode){
        viewModelScope.launch {
            unitConverterRepo.saveUnitDisplayMode(mode)
        }
    }

    // ─── 3.Language ───
//    val languageState = languageRepo.languageFlow
//    fun updateLanguage(lang: String) = viewModelScope.launch { languageRepo.saveLanguage(lang) }

    // ─── 4.Theme ───
//    val themeState = themeRepo.themeFlow
//    fun updateTheme(isDarkMode: Boolean) = viewModelScope.launch { themeRepo.saveTheme(isDarkMode) }
}