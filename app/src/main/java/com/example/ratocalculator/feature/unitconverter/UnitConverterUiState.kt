package com.example.ratocalculator.feature.unitconverter

import androidx.annotation.StringRes
import com.example.ratocalculator.R

enum class UnitCategory(@StringRes val displayNameRes: Int) {
    Mass(R.string.category_mass),
    Length(R.string.category_length),
    Temperature(R.string.category_temperature),
    Volume(R.string.category_volume),
    Area(R.string.category_area)
}

enum class UnitDisplayMode(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int
) {
    PREFIX(R.string.display_mode_prefix_title, R.string.display_mode_prefix_desc),
    POWER(R.string.display_mode_power_title, R.string.display_mode_power_desc),
    FULL_NAME(R.string.display_mode_fullname_title, R.string.display_mode_fullname_desc)
}

data class SIPrefix(
    @StringRes val nameRes: Int,
    val symbol: String,
    val power: Int
) {
    companion object {
        val NONE = SIPrefix(R.string.prefix_none, "", 0)
    }
}

data class UnitItem(
    @StringRes val nameRes: Int,
    val symbol: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
) {
    companion object {
        val DEFAULT = UnitItem(
            nameRes = R.string.unit_select,
            symbol = "",
            toBase = { it },
            fromBase = { it }
        )
    }
}

data class SideState(
    val input: String = "",
    val unit: UnitItem = UnitItem.DEFAULT,
    val prefix: SIPrefix = SIPrefix.NONE
)

data class UnitConverterUiState(
    val category: UnitCategory = UnitCategory.Mass,
    val availableUnits: List<UnitItem> = massUnits,
    val sideA: SideState = SideState(),
    val sideB: SideState = SideState()
)

data class CopyFormats(
    val prefixUnit: String = "",
    val powerUnit: String = "",
    val fullNameUnit: String = ""
)

// ==========================================
// 🔹 SI Prefixes
// ==========================================
val allSIPrefixes = listOf(
    SIPrefix(R.string.prefix_yotta, "Y", 24),
    SIPrefix(R.string.prefix_zetta, "Z", 21),
    SIPrefix(R.string.prefix_exa, "E", 18),
    SIPrefix(R.string.prefix_peta, "P", 15),
    SIPrefix(R.string.prefix_tera, "T", 12),
    SIPrefix(R.string.prefix_giga, "G", 9),
    SIPrefix(R.string.prefix_mega, "M", 6),
    SIPrefix(R.string.prefix_kilo, "k", 3),
    SIPrefix(R.string.prefix_hecto, "h", 2),
    SIPrefix(R.string.prefix_deca, "da", 1),
    SIPrefix(R.string.prefix_none, "", 0),
    SIPrefix(R.string.prefix_deci, "d", -1),
    SIPrefix(R.string.prefix_centi, "c", -2),
    SIPrefix(R.string.prefix_milli, "m", -3),
    SIPrefix(R.string.prefix_micro, "µ", -6),
    SIPrefix(R.string.prefix_nano, "n", -9),
    SIPrefix(R.string.prefix_pico, "p", -12),
    SIPrefix(R.string.prefix_femto, "f", -15),
    SIPrefix(R.string.prefix_atto, "a", -18),
    SIPrefix(R.string.prefix_zepto, "z", -21),
    SIPrefix(R.string.prefix_yocto, "y", -24)
)

// ==========================================
// 🔹 Unit List Definitions
// ==========================================
val massUnits = listOf(
    UnitItem(R.string.unit_gram, "g", { it }, { it }),
    UnitItem(R.string.unit_pound, "lb", { it * 453.59237 }, { it / 453.59237 }),
    UnitItem(R.string.unit_ounce, "oz", { it * 28.349523125 }, { it / 28.349523125 }),
    UnitItem(R.string.unit_tonne, "t", { it * 1_000_000.0 }, { it / 1_000_000.0 })
)

val lengthUnits = listOf(
    UnitItem(R.string.unit_meter, "m", { it }, { it }),
    UnitItem(R.string.unit_inch, "in", { it * 0.0254 }, { it / 0.0254 }),
    UnitItem(R.string.unit_foot, "ft", { it * 0.3048 }, { it / 0.3048 }),
    UnitItem(R.string.unit_yard, "yd", { it * 0.9144 }, { it / 0.9144 }),
    UnitItem(R.string.unit_mile, "mi", { it * 1609.344 }, { it / 1609.344 })
)

val volumeUnits = listOf(
    UnitItem(R.string.unit_liter, "L", { it }, { it }),
    UnitItem(R.string.unit_cubic_meter, "m³", { it * 1000.0 }, { it / 1000.0 }),
    UnitItem(R.string.unit_us_gallon, "gal", { it * 3.785411784 }, { it / 3.785411784 }),
    UnitItem(R.string.unit_us_quart, "qt", { it * 0.946352946 }, { it / 0.946352946 }),
    UnitItem(R.string.unit_us_pint, "pt", { it * 0.473176473 }, { it / 0.473176473 }),
    UnitItem(R.string.unit_us_cup, "c", { it * 0.2365882365 }, { it / 0.2365882365 }),
    UnitItem(R.string.unit_fluid_ounce, "fl oz", { it * 0.0295735295625 }, { it / 0.0295735295625 })
)

val temperatureUnits = listOf(
    UnitItem(R.string.unit_celsius, "°C", { it }, { it }),
    UnitItem(R.string.unit_fahrenheit, "°F", { (it - 32.0) * 5.0 / 9.0 }, { it * 9.0 / 5.0 + 32.0 }),
    UnitItem(R.string.unit_kelvin, "K", { it - 273.15 }, { it + 273.15 })
)

val areaUnits = listOf(
    UnitItem(R.string.unit_square_meter, "m²", { it }, { it }),
    UnitItem(R.string.unit_hectare, "ha", { it * 10_000.0 }, { it / 10_000.0 }),
    UnitItem(R.string.unit_acre, "ac", { it * 4046.8564224 }, { it / 4046.8564224 }),
    UnitItem(R.string.unit_square_mile, "mi²", { it * 2_589_988.110336 }, { it / 2_589_988.110336 }),
    UnitItem(R.string.unit_square_foot, "ft²", { it * 0.09290304 }, { it / 0.09290304 }),
    UnitItem(R.string.unit_square_inch, "in²", { it * 0.00064516 }, { it / 0.00064516 })
)