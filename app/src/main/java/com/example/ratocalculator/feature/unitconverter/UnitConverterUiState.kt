package com.example.ratocalculator.feature.unitconverter

import java.util.Locale

enum class UnitCategory {
    Area, Length, Temperature, Volume, Mass
}

data class UnitConverterUiState(
    val category: UnitCategory = UnitCategory.Length,
    val input: String = "",
    val fromUnit: String = "m",
    val toUnit: String = "km",
    val output: String = ""
)