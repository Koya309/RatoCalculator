package com.example.ratocalculator.feature.unitconverter

import java.util.Locale

enum class UnitCategory {
    Area, Length, Temperature, Volume, Mass
}

data class SIPrefix(
    val name: String,        // เช่น kilo
    val symbol: String,      // เช่น k
    val power: Int           // เช่น 3 (10^3)
)

data class UnitConverterUiState(
    val category: UnitCategory = UnitCategory.Length,
    val input: String = "",
    val fromUnit: String = "m",
    val toUnit: String = "km",
    val output: String = "",
    val inputValue: String = "1",     // ค่าที่ผู้ใช้กรอก
    val baseUnit: String = "gram",    // หน่วยหลัก เช่น gram
    val selectedPrefix: SIPrefix = SIPrefix("kilo", "k", 3),
    val prefixes: List<SIPrefix> = defaultPrefixes
)

val defaultPrefixes = listOf(
    SIPrefix("kilo", "k", 3),
    SIPrefix("mega", "M", 6),
    SIPrefix("giga", "G", 9),
    SIPrefix("milli", "m", -3),
    SIPrefix("micro", "µ", -6),
    SIPrefix("nano", "n", -9),
)