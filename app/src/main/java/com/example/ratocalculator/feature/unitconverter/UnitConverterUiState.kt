package com.example.ratocalculator.feature.unitconverter

data class ConverterUiState(
    val inputValue: String = "",
    val fromUnit: String = "kg",
    val toUnit: String = "g",
    val result: String = ""
)
