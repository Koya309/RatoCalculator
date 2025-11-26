package com.example.ratocalculator.feature.unitconverter

import android.location.Location.convert
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class UnitConverterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UnitConverterUiState())
    val uiState: StateFlow<UnitConverterUiState> = _uiState

    private var isUpdating = false

    fun onInputChanged(newValue: String) {
        _uiState.update { it.copy(inputValue = newValue) }
        computeConversion()
    }

    fun onPrefixASelected(prefix: SIPrefix) {
        _uiState.update { it.copy(selectedPrefixA = prefix) }
        computeConversion()
    }

    fun onUnitASelected(unit: UnitItem) {
        _uiState.update { it.copy(selectedUnitA = unit) }
        computeConversion()
    }

    fun onPrefixBSelected(prefix: SIPrefix) {
        _uiState.update { it.copy(selectedPrefixB = prefix) }
        computeConversion()
    }

    fun onUnitBSelected(unit: UnitItem) {
        _uiState.update { it.copy(selectedUnitB = unit) }
        computeConversion()
    }

    fun updateCategory(category: UnitCategory) {
        val newUnits = when (category) {
            UnitCategory.Mass -> massUnits
            UnitCategory.Temperature -> temperatureUnits
            UnitCategory.Length -> temperatureUnits
            UnitCategory.Volume -> temperatureUnits
            UnitCategory.Area -> temperatureUnits
        } //Don't Forget Fix

        _uiState.update {
            it.copy(
                category = category,
                availableUnits = newUnits,
                selectedUnitA = newUnits.first(),
                selectedUnitB = newUnits.last()
            )
        }

        computeConversion()
    }

    private fun computeConversion() {
        val state = _uiState.value

        val input = state.inputValue.toDoubleOrNull() ?: run {
            _uiState.update { it.copy(outputValue = "") }
            return
        }
/*
        // A → base (กรัม)
        val baseValue = input *
                10.0.pow(state.selectedPrefixA.power) *
                state.selectedUnitA.multiplier

        // base → B
        val output =
            baseValue / state.selectedUnitB.multiplier / 10.0.pow(state.selectedPrefixB.power)

        // เก็บผลลัพธ์เป็น string สวย ๆ
        val text = output.toString()

        _uiState.update {
            it.copy(outputValue = text)
        }
*/    }

    fun onInputChanged(newValue: String, prefix: SIPrefix) {
        _uiState.update { it.copy(inputValue = newValue, selectedPrefix = prefix) }
    }

    fun onPrefixSelected(prefix: SIPrefix) {
        _uiState.update { it.copy(selectedPrefix = prefix) }
    }

    fun onUnitSelected(unit: UnitItem) {
        _uiState.update {
            it.copy(selectedUnit = unit)
        }
    }



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
        return power.toString().map { superscripts[it] ?: it }.joinToString("")
    }
}