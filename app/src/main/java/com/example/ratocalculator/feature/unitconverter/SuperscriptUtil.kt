package com.example.ratocalculator.feature.unitconverter


fun toSuperscript(power: Int): String {
    val superscripts = mapOf(
        '0' to '⁰',
        '1' to '¹',
        '2' to '²',
        '3' to '³',
        '4' to '⁴',
        '5' to '⁵',
        '6' to '⁶',
        '7' to '⁷',
        '8' to '⁸',
        '9' to '⁹',
        '-' to '⁻'
    )
    return power.toString()
        .map { superscripts[it] ?: it }
        .joinToString("")
}
