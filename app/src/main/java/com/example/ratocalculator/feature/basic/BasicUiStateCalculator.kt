package com.example.ratocalculator.feature.basic

data class BasicUiStateCalculator(
    val input: String = "",
    val result: String = "",
    val decimalPlaces: Int = 2,
    val removeTrailingZero: Boolean = true
)