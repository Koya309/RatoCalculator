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

    fun updateCategory(category: UnitCategory) {
        _uiState.update { it.copy(category = category) }
        // reset ค่า default ของหน่วย ตาม category
        when (category) {
            UnitCategory.Length -> _uiState.update { it.copy(fromUnit = "m", toUnit = "km") }
            UnitCategory.Mass -> _uiState.update { it.copy(fromUnit = "kg", toUnit = "g") }
            else -> {}
        }
        recalc()
    }

    fun updateFromUnit(unit: String) {
        _uiState.update { it.copy(fromUnit = unit) }
        recalc()
    }

    fun updateToUnit(unit: String) {
        _uiState.update { it.copy(toUnit = unit) }
        recalc()
    }

    fun swapUnits() {
        _uiState.update { it.copy(fromUnit = it.toUnit, toUnit = it.fromUnit) }
        recalc()
    }

    fun onNumberClick(number: String) {
        _uiState.update { it.copy(input = it.input + number) }
        recalc()
    }

    fun clearInput() {
        _uiState.update { it.copy(input = "") }
        recalc()
    }

    private fun recalc() {
        val state = _uiState.value
        val inputVal = state.input.toDoubleOrNull() ?: 0.0

        val result = when (state.category) {
            UnitCategory.Length -> convertLength(inputVal, state.fromUnit, state.toUnit)
            UnitCategory.Mass -> convertMass(inputVal, state.fromUnit, state.toUnit)
            else -> inputVal
        }

        _uiState.update { it.copy(output = result.toString()) }
    }

    private fun convertLength(value: Double, from: String, to: String): Double {
        return when (from to to) {
            "m" to "km" -> value / 1000
            "km" to "m" -> value * 1000
            else -> value
        }
    }

    private fun convertMass(value: Double, from: String, to: String): Double {
        return when (from to to) {
            "kg" to "g" -> value * 1000
            "g" to "kg" -> value / 1000
            else -> value
        }
    }
}