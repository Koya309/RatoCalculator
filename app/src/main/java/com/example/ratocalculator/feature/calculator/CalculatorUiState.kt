package com.example.ratocalculator.feature.calculator

data class CalculatorState(
    val input: String = "",
    val result: String = "",
    val decimalPlaces: Int = 2,
    val removeTrailingZero: Boolean = true
)