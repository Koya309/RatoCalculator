package com.example.ratocalculator.feature.unitconverter

enum class UnitCategory(val displayName: String) {
    Mass("Mass"),
    Length("Length"),
    Temperature("Temperature"),
    Volume("Volume"),
    Area("Area")
}

data class SIPrefix(
    val name: String,
    val symbol: String,
    val power: Int
) {
    companion object {
        val NONE = SIPrefix("", "", 0)
    }
}

data class UnitItem(
    val name: String,
    val symbol: String,

    // เทียบกับหน่วยฐานของหมวด (kg, m, s ฯลฯ)
    val multiplier: Double
)

data class ConverterSideState(
    val input: String = "",
    val prefix: SIPrefix = SIPrefix.NONE,
    val unit: UnitItem
)

data class UnitConverterUiState(

    val category: UnitCategory = UnitCategory.Mass,
    val availableUnits: List<UnitItem> = massUnits,

    val sideA: ConverterSideState = ConverterSideState(
        unit = massUnits.first()
    ),

    val sideB: ConverterSideState = ConverterSideState(
        unit = massUnits[1]
    )
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
    SIPrefix(" ", "", 0),
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

val massUnits = listOf(
    UnitItem("gram", "g", { it }, { it }),
    UnitItem("pound", "lb", { it * 453.592 }, { it / 453.592 }),
    UnitItem("ounce", "oz", { it * 28.3495 }, { it / 28.3495 }),
    UnitItem("tonne", "t", { it * 1_000_000 }, { it / 1_000_000 })
)

val lengthUnits = listOf(
    UnitItem("Celsius", "°C",
        toBase = { it },                   // ฐานคือ °C
        fromBase = { it }
    ),
    UnitItem("Fahrenheit", "°F",
        toBase = { (it - 32) * 5 / 9 },
        fromBase = { it * 9 / 5 + 32 }
    ),
    UnitItem("Kelvin", "K",
        toBase = { it - 273.15 },
        fromBase = { it + 273.15 }
    )
)

val volumeUnits = listOf(
    UnitItem("Celsius", "°C",
        toBase = { it },                   // ฐานคือ °C
        fromBase = { it }
    ),
    UnitItem("Fahrenheit", "°F",
        toBase = { (it - 32) * 5 / 9 },
        fromBase = { it * 9 / 5 + 32 }
    ),
    UnitItem("Kelvin", "K",
        toBase = { it - 273.15 },
        fromBase = { it + 273.15 }
    )
)

val temperatureUnits = listOf(
    UnitItem("Celsius", "°C",
        toBase = { it },                   // ฐานคือ °C
        fromBase = { it }
    ),
    UnitItem("Fahrenheit", "°F",
        toBase = { (it - 32) * 5 / 9 },
        fromBase = { it * 9 / 5 + 32 }
    ),
    UnitItem("Kelvin", "K",
        toBase = { it - 273.15 },
        fromBase = { it + 273.15 }
    )
)

val areaUnits = listOf(
    UnitItem("Celsius", "°C",
        toBase = { it },                   // ฐานคือ °C
        fromBase = { it }
    ),
    UnitItem("Fahrenheit", "°F",
        toBase = { (it - 32) * 5 / 9 },
        fromBase = { it * 9 / 5 + 32 }
    ),
    UnitItem("Kelvin", "K",
        toBase = { it - 273.15 },
        fromBase = { it + 273.15 }
    )
)