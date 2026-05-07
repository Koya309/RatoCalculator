package com.example.ratocalculator.feature.basic


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
     savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorState())
    private val operators = listOf("+", "-", "×", "÷")
    val uiState: StateFlow<CalculatorState> = _uiState

    fun onButtonClick(
        label: String,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ) {
        when (label) {
            "C" -> {
                _uiState.update { CalculatorState() }
            }

            "DEL" -> {
                val currentInput = _uiState.value.input
                if (currentInput.isNotEmpty()) {
                    val newInput = currentInput.dropLast(1)
                    _uiState.update { it.copy(input = newInput) }
                    calculateResult(decimalPlaces, removeTrailingZero)
                }
            }

            in operators -> {
                handleOperator(label, decimalPlaces, removeTrailingZero)
            }

            "=" -> {
                val currentResult = _uiState.value.result
                if (currentResult.isNotEmpty()) {

                    _uiState.update { it.copy(input = currentResult, result = "") }

                    //Room (History)
                }
            }

            "( )" -> {
                handleParentheses()
            }

            "." -> enterDecimal()

            "+/-" -> toggleSign()

        else -> {
            val currentInput = _uiState.value.input
            val newInput = currentInput + label
            _uiState.update { it.copy(input = newInput) }

            calculateResult(decimalPlaces, removeTrailingZero)
            }
        }
    }
    @Suppress("UNUSED_PARAMETER")
    private fun handleOperator(
        label: String,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ){
        val currentInput = _uiState.value.input
        if (currentInput.isEmpty()) {
            if (label == "-") _uiState.update { it.copy(input = "-") }
            return
        }
        val lastChar = currentInput.last().toString()
        val allOps = listOf("+", "-", "×", "÷", "*", "/")

        if (lastChar in allOps) {
            _uiState.update { it.copy(input = currentInput.dropLast(1) + label) }
        } else {
            _uiState.update { it.copy(input = currentInput + label) }
        }

        // สำคัญ: ทุกครั้งที่กดเครื่องหมาย ให้ลองสั่งคำนวณ Preview ด้วย
        calculateResult(decimalPlaces, removeTrailingZero)
    }

    private fun calculateResult(
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ){
        val currentInput = _uiState.value.input

        val lastChar = currentInput.lastOrNull()
        if (currentInput.isEmpty() || lastChar in listOf('+', '-', '×', '÷', '.', '(')) {
            _uiState.update { it.copy(result = "") }
            return
        }

        try {
            val expressionStr = prepareExpression(currentInput)

            val expression = ExpressionBuilder(expressionStr).build()
            val result = expression.evaluate()
            val formatted = formatResult(result, decimalPlaces, removeTrailingZero)

            _uiState.update { it.copy(result = formatted) }
        } catch (_: Exception) {
            _uiState.update { it.copy(result = "") }
        }
    }

    private fun performCalculation(): String {
        val currentInput = _uiState.value.input
        if (currentInput.isEmpty()) return ""

        try {
            val expressionStr = prepareExpression(currentInput)

            if (expressionStr.count { it == '(' } != expressionStr.count { it == ')' }) return ""

            val expression = ExpressionBuilder(expressionStr).build()
            val result = expression.evaluate()

            return formatResult(result, 2, true)
        } catch (_: Exception) {
            return ""
        }
    }


    private fun handleParentheses(){
        val currentInput = _uiState.value.input

        val openBrackets = currentInput.count { it == '(' }
        val closeBrackets = currentInput.count { it == ')' }

        val lastChar = currentInput.lastOrNull()
        val operators = listOf('+', '-', '*', '/', '×', '÷')

        val charToAdd = when {
            currentInput.isEmpty() || lastChar in operators || lastChar == '(' -> "("

            openBrackets > closeBrackets && (lastChar?.isDigit() == true || lastChar == ')') -> ")"

            lastChar?.isDigit() == true -> "("

            else -> "("
        }
        _uiState.update { it.copy(input = currentInput + charToAdd) }
    }

    private fun prepareExpression(rawInput: String): String{
        var prepared = rawInput
            .replace("×", "*")
            .replace("÷", "/")
            .replace(",", "")
            .replace(Regex("(\\d)\\("), "$1*(") // 5( เป็น 5*(
            .replace(Regex("\\)(\\d)"), ")*$1") // )5 เป็น )*5
            .replace(Regex("\\)\\("), ")*(")

        val openCount = prepared.count{it == '('}
        val closeCount = prepared.count{it == ')'}

        if (openCount > closeCount){
            val lastChar = prepared.lastOrNull()
            val operators = listOf('+', '-', '*', '/')

            if (lastChar != null && lastChar !in  operators && lastChar != '(') {
                repeat(openCount - closeCount){
                    prepared += ")"
                }
            }
        }
        return prepared
    }

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

    private fun enterDecimal() {
        val input = _uiState.value.input
        if (input.isEmpty() || input.last() in listOf('+', '-', '×', '÷', '(')) {
            _uiState.update { it.copy(input = input + "0.") }
            return
        }

        val lastOperatorIndex = input.lastIndexOfAny(charArrayOf('+', '-', '*', '/', '×', '÷', '(', ')'))
        val lastNumberSegment = if (lastOperatorIndex == -1) input else input.substring(lastOperatorIndex +1)

        if (!lastNumberSegment.contains('.')){
            _uiState.update { it.copy(input = "$input.")}
        }
    }

    private fun toggleSign() {
        val input = _uiState.value.input
        if (input.isEmpty()) return

        val lastOpIndex = input.lastIndexOfAny(charArrayOf('+', '×', '÷', '(', ')'))

        val actualOpIndex = if (lastOpIndex == -1 && input.startsWith("-")) -1 else lastOpIndex

        val prefix = if (actualOpIndex == -1) "" else input.substring(0, actualOpIndex + 1)
        val lastPart = if (actualOpIndex == -1) input else input.substring(actualOpIndex + 1)

        if (lastPart.isEmpty()) return

        // 🚩 หัวใจสำคัญ: สลับเครื่องหมาย (Toggle)
        val updatedLastPart = if (lastPart.startsWith("-")) {
            lastPart.removePrefix("-")
        } else {
            "-$lastPart"
        }

        _uiState.update { it.copy(input = prefix + updatedLastPart) }

        calculateResult(2, true)
    }

    init {
        _uiState.value = _uiState.value.copy(
            input = savedStateHandle["lastInput"] ?: ""
        )
    }
}