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
    private val _savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorState())
    private val operators = listOf("+", "-", "*", "/")
    val uiState: StateFlow<CalculatorState> = _uiState

    fun onButtonClick(
        label: String,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ){
        when(label){
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
                if (currentResult.isNotEmpty()){
                    _uiState.update { it.copy(
                        input = currentResult,
                        result = ""
                    ) }
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
    private fun handleOperator(
        label: String,
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ){
        val state = _uiState.value
        val lastChar = state.input.lastOrNull()?.toString() ?: ""

        when {
            state.result.isNotEmpty() -> {
                _uiState.update { it.copy(input = state.result + label, result = "") }
            }

            state.input.isEmpty() && label == "-" -> {
                _uiState.update { it.copy(input = "-") }
            }

            lastChar in operators && label != "-" -> {
                _uiState.update { it.copy(input = state.input.dropLast(1) + label) }
            }

            lastChar.isNotEmpty() && lastChar !in operators -> {
                _uiState.update { it.copy(input = state.input + label) }
            }
        }
    }

    private fun calculateResult(
        decimalPlaces: Int,
        removeTrailingZero: Boolean
    ){
        val currentInput = _uiState.value.input
        if (currentInput.isEmpty()){
            _uiState.update { it.copy(result = "") }
            return
        }

        val lastChar = currentInput.last().toString()
        if (lastChar in operators || lastChar == ".") return

        try {
            val expressionStr = prepareExpression(currentInput)

            val expression = ExpressionBuilder(expressionStr).build()
            val result = expression.evaluate()

            val formatted = formatResult(result, decimalPlaces, removeTrailingZero)
            _uiState.update { it.copy(result = formatted) }
        } catch (e: Exception) {
            _uiState.update { it.copy(result = "") }
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
            repeat(openCount - closeCount) {
                prepared += ")"
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