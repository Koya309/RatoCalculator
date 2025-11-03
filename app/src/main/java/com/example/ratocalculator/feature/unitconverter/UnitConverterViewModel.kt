package com.example.ratocalculator.feature.unitconverter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UnitConverterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UnitConverterUiState())
    val uiState: StateFlow<UnitConverterUiState> = _uiState

    fun onInputChanged(newValue: String, prefix: SIPrefix) {
        _uiState.update { it.copy(inputValue = newValue, selectedPrefix = prefix) }
    }

    fun onPrefixSelected(prefix: SIPrefix) {
        _uiState.update { it.copy(selectedPrefix = prefix) }
    }

    fun onUnitSelected(unit: UnitItem) {
        _uiState.update {
            it.copy(selectedUnit = unit)
        }
    }


    fun updateCategory(category: UnitCategory) {
        val newUnits = when (category) {
            UnitCategory.Mass -> massUnits
            UnitCategory.Temperature -> temperatureUnits
            else -> emptyList()
        }
        _uiState.update {
            it.copy(
                category = category,
                availableUnits = newUnits,
                selectedUnit = newUnits.firstOrNull() ?: it.selectedUnit
            )
        }
    }

    fun toSuperscript(power: Int): String {
        val superscripts = mapOf(
            '0' to '⁰',
            '1' to '¹',
            '2' to '²',
            '3' to '³',
            '4' to '⁴',
            '5' to '⁵',
            '6' to '⁶',
            '7' to '⁷',
            '8' to '⁸',
            '9' to '⁹',
            '-' to '⁻'
        )
        return power.toString().map { superscripts[it] ?: it }.joinToString("")
    }
}