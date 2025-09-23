package com.example.ratocalculator.feature.basic

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.objecthunter.exp4j.ExpressionBuilder

@HiltViewModel
class CalculatorViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState

    // ✅ จัด format ตาม settings
    fun formatResult(
        result: Double,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ): String {
        return if (removeTrailingZero && result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            "%.${decimalPlaces}f".format(result)
        }
    }

    // ✅ คำนวณโดยรับค่า settings จากภายนอก
    private fun calculateResult(decimalPlaces: Int, removeTrailingZero: Boolean) {
        try {
            val expression = _uiState.value.input
                .replace("×", "*")
                .replace("÷", "/")

            val result = ExpressionBuilder(expression).build().evaluate()
            _uiState.value = _uiState.value.copy(
                result = formatResult(result, decimalPlaces, removeTrailingZero)
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(result = "Error")
        }
    }

    // ✅ ใช้ decimalPlaces + removeTrailingZero ที่ส่งมาจาก Screen
    fun onButtonClick(
        label: String,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ) {
        val input = _uiState.value.input
        val operators = listOf("+", "-", "*", "/")
        val lastChar = input.lastOrNull()?.toString() ?: ""

        when (label) {
            in operators -> {
                if (lastChar !in operators && input.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(input = input + label)
                }
            }
            "=" -> calculateResult(decimalPlaces, removeTrailingZero)
            "C" -> _uiState.value = CalculatorUiState()
            "DEL" -> deleteLast()
            "." -> enterDecimal()
            "x10" -> double10()
            else -> _uiState.value = _uiState.value.copy(input = input + label)
        }
    }

    // ✅ helper functions
    private fun deleteLast() {
        val input = _uiState.value.input
        if (input.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(input = input.dropLast(1))
        }
    }

    private fun enterDecimal() {
        val input = _uiState.value.input
        if (input.isNotEmpty() && input.last() != '.') {
            _uiState.value = _uiState.value.copy(input = input + ".")
        }
    }
    private fun double10() {
        val input = _uiState.value.input
        if (input.isNotEmpty() && !input.endsWith("*10")) {
            _uiState.value = _uiState.value.copy(input = input + "*10")
        }
    }
}