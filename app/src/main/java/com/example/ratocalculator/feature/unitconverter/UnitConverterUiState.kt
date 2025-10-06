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
    val category: UnitCategory = UnitCategory.Mass,
    val inputValue: String = "0",

    // A = Prefix
    val selectedPrefix: SIPrefix = SIPrefix("kilo", "k", 3),
    val prefixes: List<SIPrefix> = allSIPrefixes,

    // B = SI Unit
    val selectedUnit: String = "g",
    val units: List<String> = listOf("g", "kg"),

    val fromUnit: String = "m",
    val toUnit: String = "km",
    val output: String = "",
    val baseUnit: String = "gram",

)

val siUnitsMap = mapOf(
    UnitCategory.Mass to listOf("gram"),
    UnitCategory.Length to listOf("meter"),
    UnitCategory.Area to listOf("square meter"),
    UnitCategory.Volume to listOf("liter"),
    UnitCategory.Temperature to listOf("celsius")
)

val allSIPrefixes = listOf(
    SIPrefix("yotta", "Y", 24),
    SIPrefix("zetta", "Z", 21),
    SIPrefix("exa", "E", 18),
    SIPrefix("peta", "P", 15),
    SIPrefix("tera", "T", 12),
    SIPrefix("giga", "G", 9),
    SIPrefix("mega", "M", 6),
    SIPrefix("kilo", "k", 3),
    SIPrefix("hecto", "h", 2),
    SIPrefix("deca", "da", 1),
    SIPrefix("base", "", 0),
    SIPrefix("deci", "d", -1),
    SIPrefix("centi", "c", -2),
    SIPrefix("milli", "m", -3),
    SIPrefix("micro", "µ", -6),
    SIPrefix("nano", "n", -9),
    SIPrefix("pico", "p", -12),
    SIPrefix("femto", "f", -15),
    SIPrefix("atto", "a", -18),
    SIPrefix("zepto", "z", -21),
    SIPrefix("yocto", "y", -24)
)