package com.example.ratocalculator.feature.basic

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import net.objecthunter.exp4j.ExpressionBuilder

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val _savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(BasicUiStateCalculator())
    val uiState: StateFlow<BasicUiStateCalculator> = _uiState

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

    private fun calculateResult(decimalPlaces: Int, removeTrailingZero: Boolean) {
        val currentInput = _uiState.value.input


        Log.d("CalculatorDebug", "Step 1: Input is '$currentInput'")

        if (currentInput.isBlank()){
            _uiState.update { it.copy(result = "") }
            return
        }

        try {
            val expression = currentInput
                .replace("×", "*")
                .replace("÷", "/")
                .replace(",", "")

            Log.d("CalculatorDebug", "Step 2: Cleaned Expression is '$expression'")

            val result = ExpressionBuilder(expression).build().evaluate()

            val formatted = formatResult(result, decimalPlaces , removeTrailingZero)

            Log.d("CalculatorDebug", "Step 3: Calculated Result is '$formatted'")
            _uiState.update {
                it.copy(result = formatted)
            }
        } catch (e: Exception) {
            Log.e("CalculatorDebug", "Error: ${e.message}")
            _uiState.update { it.copy(result = "") }
        }
    }

    fun onButtonClick(
        label: String,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ) {
        val input = _uiState.value.input
        val result = _uiState.value.result
        val operators = listOf("+", "-", "*", "/")

        when (label) {
            in operators -> {
                val input = _uiState.value.input
                val lastChar = input.lastOrNull()?.toString() ?: ""
                if (input.isEmpty()){
                    if (label == "-") _uiState.value = _uiState.value.copy(input = "-")
                }
                else if (result.isNotEmpty() && input.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        input = result + label,
                        result = ""
                    )
                }
                else if (lastChar in listOf("+", "*", "/", "×", "÷") && label == "-"){
                    _uiState.value = _uiState.value.copy(input = input + label)
                }
                else if (lastChar !in operators && input.isNotEmpty()){
                    _uiState.value = _uiState.value.copy(input = input + label)
                }
            }
            "=" -> {
                calculateResult(decimalPlaces, removeTrailingZero)

                val newResult = _uiState.value.result
                if (newResult.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        input = newResult,
                        result = ""
                    )
                }
            }
            "C" -> _uiState.value = BasicUiStateCalculator()
            "DEL" -> deleteLast()
            "." -> enterDecimal()
            "x10" -> double10()
            "+/-" -> toggleSign()

            else -> _uiState.value = _uiState.value.copy(input = input + label)
        }
    }

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
    private fun toggleSign() {
        val input = _uiState.value.input
        if (input.isEmpty()) return

        val lastOperatorIndex = input.lastIndexOfAny(charArrayOf('+', '-', '*', '/', '×', '÷'))

        val updatedInput = if (lastOperatorIndex == -1) {
            if (input.startsWith("-")) input.removePrefix("-") else "-$input"
        } else if (lastOperatorIndex == 0 && input.startsWith("-")) {
            input.removePrefix("-")
        } else {
            val prefix = input.substring(0, lastOperatorIndex + 1)
            val lastNumber = input.substring(lastOperatorIndex + 1)

            if (lastNumber.isEmpty()) input // ถ้ากด +/- หลังเครื่องหมายพอดี ไม่ต้องทำอะไร
            else prefix + "-" + lastNumber
        }
        _uiState.value = _uiState.value.copy(input = updatedInput)
    }

    init {
        _uiState.value = _uiState.value.copy(
            input = _savedStateHandle["lastInput"] ?: ""
        )
    }
}