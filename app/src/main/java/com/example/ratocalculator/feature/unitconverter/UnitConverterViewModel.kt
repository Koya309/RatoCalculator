package com.example.ratocalculator.feature.unitconverter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.pow

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

    fun onUnitSelected(unit: String) {
        _uiState.update { it.copy(selectedUnit = unit) }
    }

    fun updateCategory(category: UnitCategory) {
        val units = when (category) {
            UnitCategory.Length -> listOf("m", "km")
            UnitCategory.Mass -> listOf("g", "kg")
            UnitCategory.Volume -> listOf("L", "mL")
            UnitCategory.Temperature -> listOf("C", "K")
            UnitCategory.Area -> listOf("m²", "cm²")
        }
        _uiState.update {
            it.copy(
                category = category,
                units = units,
                selectedUnit = units.first()
            )
        }
    }
}