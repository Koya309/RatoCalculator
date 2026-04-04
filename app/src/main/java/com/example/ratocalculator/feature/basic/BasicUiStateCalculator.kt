package com.example.ratocalculator.feature.basic

data class CalculatorState(
    val input: String = "",
    val result: String = "",
    val decimalPlaces: Int = 2,
    val removeTrailingZero: Boolean = true
)