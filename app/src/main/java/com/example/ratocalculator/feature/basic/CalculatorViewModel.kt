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

        if (currentInput.isBlank()){
            _uiState.update { it.copy(result = "") }
            return
        }

        try {
            val expression = currentInput
                .replace("×", "*")
                .replace("÷", "/")
                .replace(",", "")

            val result = ExpressionBuilder(expression).build().evaluate()

            val formatted = formatResult(result, decimalPlaces , removeTrailingZero)

            _uiState.update {
                it.copy(result = formatted)
            }
        } catch (e: Exception) {
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
                val currentInput = _uiState.value.input
                val currentResult = _uiState.value.result
                val state =_uiState.value
                val lastChar = currentInput.lastOrNull()?.toString() ?: ""

                when {
                    // เคส 1: มีผลลัพธ์เก่าอยู่ (Ans) แล้วกดเครื่องหมายต่อ ให้ดึงผลลัพธ์มาเป็นตัวตั้ง
                    state.result.isNotEmpty() -> {
                        _uiState.update { it.copy(
                            input = state.result + label,
                            result = ""
                        ) }
                    }

                    // เคส 2: หน้าจอว่าง แต่อยากพิมพ์เลขติดลบ (เช่น -5)
                    state.input.isEmpty() && label == "-" -> {
                        _uiState.update { it.copy(input = "-") }
                    }

                    // เคส 3: ตัวสุดท้ายเป็นเครื่องหมายอยู่แล้ว แต่อยากใส่ลบเพื่อทำเลขติดลบ (เช่น 5 * -)
                    lastChar in listOf("+", "*", "/", "×", "÷") && label == "-" -> {
                        _uiState.update { it.copy(input = state.input + label) }
                    }

                    // เคส 4: ตัวสุดท้ายไม่ใช่เครื่องหมาย (เป็นตัวเลข) ก็ใส่เครื่องหมายต่อท้ายได้เลย
                    lastChar.isNotEmpty() && lastChar !in operators -> {
                        _uiState.update { it.copy(input = state.input + label) }
                    }
                }

                // สำคัญ: เรียกคำนวณทันทีหลังจาก Update Input เพื่อให้ Result แสดง Auto
                if (_uiState.value.input.isNotEmpty()) {
                    calculateResult(decimalPlaces, removeTrailingZero)
                }
            }


            "=" -> {
                calculateResult(decimalPlaces, removeTrailingZero)
            }
            "C" -> _uiState.value = BasicUiStateCalculator()
            "DEL" -> {
                deleteLast()
                calculateResult(decimalPlaces, removeTrailingZero)
            }
            "." -> enterDecimal()
            "x10" -> double10()
            "+/-" -> toggleSign()

            else -> {
                val newInput = _uiState.value.input + label
                _uiState.value = _uiState.value.copy(input = input + label)
            }
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

            if (lastNumber.isEmpty()) input
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