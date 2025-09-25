package com.example.ratocalculator.feature.unitconverter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UnitConverterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState: StateFlow<ConverterUiState> = _uiState

    fun updateInput(value: String) {
        _uiState.value = _uiState.value.copy(
            inputValue = value,
            result = calculate(value,
                _uiState.value.fromUnit,
                _uiState.value.toUnit)
        )
    }

    fun updateUnits(from: String, to: String) {
        _uiState.value = _uiState.value.copy(
            fromUnit = from,
            toUnit = to,
            result = calculate(_uiState.value.inputValue, from, to)
        )
    }

    private fun calculate(value: String, from: String, to: String): String {
        val number = value.toDoubleOrNull() ?: return ""
        return when (from to to) {
            "kg" to "g" -> (number * 1000).toString()
            "g" to "kg" -> (number / 1000).toString()
            else -> value
        }
    }






}