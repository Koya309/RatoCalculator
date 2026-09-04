package com.example.ratocalculator.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratocalculator.data.repository.CalculatorRepository
import com.example.ratocalculator.data.repository.SystemRepository
import com.example.ratocalculator.data.repository.UnitConverterRepository
import com.example.ratocalculator.feature.unitconverter.UnitDisplayMode
import com.example.ratocalculator.settings.model.AppLanguage
import com.example.ratocalculator.settings.model.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ResetOption {
    BASIC_CALCULATOR,
    UNIT_CONVERTER,
    THEME_AND_LANGUAGE,
    ALL_SETTINGS
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val systemRepo: SystemRepository,
    private val calculatorRepo: CalculatorRepository,
    private val unitConverterRepo: UnitConverterRepository
) : ViewModel() {

    // System
    val appTheme: StateFlow<AppTheme> = systemRepo.appTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    fun updateAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            systemRepo.saveAppTheme(theme)
        }
    }

    val appLanguage: StateFlow<AppLanguage> = systemRepo.appLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppLanguage.SYSTEM
        )

    fun updateAppLanguage(language: AppLanguage){
        viewModelScope.launch {
            systemRepo.saveAppLanguage(language)
        }
    }

    // Reset
    fun resetCalculatorToDefault() {
        viewModelScope.launch {
            calculatorRepo.saveDecimalPlaces(2)
            calculatorRepo.saveRemoveTrailingZero(true)
        }
    }

    fun resetUnitConverterToDefault() {
        viewModelScope.launch {
            unitConverterRepo.saveSideBlock(false)
            unitConverterRepo.saveAdvancedPrefix(false)
            unitConverterRepo.saveUnitDisplayMode(UnitDisplayMode.FULL_NAME)
        }
    }

    fun resetThemeAndLanguageToDefault() {
        viewModelScope.launch {
            systemRepo.saveAppTheme(AppTheme.SYSTEM)
            systemRepo.saveAppLanguage(AppLanguage.SYSTEM)
        }
    }

    fun resetAllToDefault() {
        resetCalculatorToDefault()
        resetUnitConverterToDefault()
        resetThemeAndLanguageToDefault()
    }

    fun resetByOption(option: ResetOption) {
        when (option) {
            ResetOption.BASIC_CALCULATOR -> resetCalculatorToDefault()
            ResetOption.UNIT_CONVERTER -> resetUnitConverterToDefault()
            ResetOption.THEME_AND_LANGUAGE -> resetThemeAndLanguageToDefault()
            ResetOption.ALL_SETTINGS -> resetAllToDefault()
        }
    }

    // ─── 1.Basic Calculator ───
    val decimalPlaces: StateFlow<Int> = calculatorRepo.decimalPlaces
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 2
        )

    val removeTrailingZero: StateFlow<Boolean> = calculatorRepo.removeTrailingZero
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun updateDecimalPlaces(value: Int) {
        viewModelScope.launch {
            calculatorRepo.saveDecimalPlaces(value)
        }
    }

    fun updateRemoveTrailingZero(value: Boolean) {
        viewModelScope.launch {
            calculatorRepo.saveRemoveTrailingZero(value)
        }
    }


    // ─── 2.Unit Converter ───
    val sideBlock: StateFlow<Boolean> = unitConverterRepo.sideBlock
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val AdvancedPrefix: StateFlow<Boolean> = unitConverterRepo.AdvancedPrefix
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val unitDisplayMode: StateFlow<UnitDisplayMode> = unitConverterRepo.unitDisplayMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UnitDisplayMode.FULL_NAME
        )

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

    fun updateUnitDisplayMode(mode: UnitDisplayMode) {
        viewModelScope.launch {
            unitConverterRepo.saveUnitDisplayMode(mode)
        }
    }
}