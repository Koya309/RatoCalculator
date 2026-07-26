package com.example.ratocalculator.feature.unitconverter

enum class UnitCategory(val displayName: String) {
    Mass("Mass"),
    Length("Length"),
    Temperature("Temperature"),
    Volume("Volume"),
    Area("Area")
}

enum class UnitDisplayMode(
    val displayName: String,
    val description: String
){
    PREFIX("Prefix Unit", "แสดงหน่วยแบบคำนำหน้า เช่น 250 kg"),
    POWER("Power Unit", "แสดงหน่วยแบบสัญลักษณ์สิบยกกำลัง เช่น 250 × 10³ g"),
    FULL_NAME("Full Name Unit", "แสดงชื่อหน่วยเต็ม เช่น 250 kilo gram")
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
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double

){
    companion object{
        val DEFAULT = UnitItem(
            name = "Select Unit",
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
    val fullNameUnit: String =""
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
    UnitItem("gram", "g",
        { it }, { it }
    ),
    UnitItem("pound", "lb",
        { it * 453.592 }, { it / 453.592 }
    ),
    UnitItem("ounce", "oz",
        { it * 28.3495 }, { it / 28.3495 }
    ),
    UnitItem("tonne", "t",
        { it * 1_000_000 }, { it / 1_000_000 }
    )
)

val lengthUnits = listOf(
    UnitItem("meter", "m",
        toBase = { it },                    // หน่วยฐาน
        fromBase = { it }
    ),
    UnitItem("Inch", "in",
        toBase = { it * 0.0254 },           // 1 inch = 0.0254 m
        fromBase = { it / 0.0254 }
    ),
    UnitItem("Foot", "ft",
        toBase = { it * 0.3048 },           // 1 foot = 0.3048 m
        fromBase = { it / 0.3048 }
    ),
    UnitItem("Yard", "yd",
        toBase = { it * 0.9144 },           // 1 yard = 0.9144 m
        fromBase = { it / 0.9144 }
    ),
    UnitItem("Mile", "mi",
        toBase = { it * 1609.344 },         // 1 mile = 1609.344 m
        fromBase = { it / 1609.344 }
    )
)

val volumeUnits = listOf(
    UnitItem("Liter", "L",
        toBase = { it },                    // หน่วยฐาน
        fromBase = { it }
    ),
    UnitItem("Cubic Meter", "m³",
        toBase = { it * 1000.0 },           // 1 m³ = 1000 L
        fromBase = { it / 1000.0 }
    ),
    UnitItem("US Gallon", "gal",
        toBase = { it * 3.78541 },          // 1 gal ≈ 3.785 L
        fromBase = { it / 3.78541 }
    ),
    UnitItem("US Quart", "qt",
        toBase = { it * 0.94635 },          // 1 qt ≈ 0.946 L
        fromBase = { it / 0.94635 }
    ),
    UnitItem("US Pint", "pt",
        toBase = { it * 0.47318 },          // 1 pt ≈ 0.473 L
        fromBase = { it / 0.47318 }
    ),
    UnitItem("US Cup", "c",
        toBase = { it * 0.23659 },          // 1 cup ≈ 237 mL หรือ 0.236 L
        fromBase = { it / 0.23659 }
    ),
    UnitItem("Fluid Ounce", "fl oz",
        toBase = { it * 0.02957 },          // 1 fl oz ≈ 29.57 mL
        fromBase = { it / 0.02957 }
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
    UnitItem("Square Meter", "m²",
        toBase = { it },                    // หน่วยฐาน
        fromBase = { it }
    ),
    UnitItem("Hectare", "ha",
        toBase = { it * 10_000.0 },         // 1 ha = 10_000 m²
        fromBase = { it / 10_000.0 }
    ),
    UnitItem("Acre", "ac",
        toBase = { it * 4046.856 },         // 1 acre ≈ 4046.86 m²
        fromBase = { it / 4046.856 }
    ),
    UnitItem("Square Mile", "mi²",
        toBase = { it * 2_589_988.11 },     // 1 mi² ≈ 2.59 km²
        fromBase = { it / 2_589_988.11 }
    ),
    UnitItem("Square Foot", "ft²",
        toBase = { it * 0.092903 },         // 1 ft² ≈ 0.0929 m²
        fromBase = { it / 0.092903 }
    ),
    UnitItem("Square Inch", "in²",
        toBase = { it * 0.00064516 },       // 1 in² = 6.4516 cm²
        fromBase = { it / 0.00064516 }
    )
)