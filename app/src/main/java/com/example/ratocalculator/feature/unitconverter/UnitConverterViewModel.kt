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

    fun onCategoryChanged(category: UnitCategory) {
        val units = when (category) {
            UnitCategory.Mass -> massUnits
            UnitCategory.Length -> lengthUnits
            UnitCategory.Area -> areaUnits
            UnitCategory.Volume -> volumeUnits
        }

        _uiState.update {
            it.copy(
                category = category,
                availableUnits = units,
                sideA = it.sideA.copy(unit = units.first()),
                sideB = it.sideB.copy(unit = units[1])
            )
        }

        convertAtoB()
    }

    fun onInputAChanged(value: String) {
        if (isUpdating) return

        _uiState.update {
            it.copy(sideA = it.sideA.copy(input = value))
        }
        convertAtoB()
    }

    fun onPrefixAChanged(prefix: SIPrefix) {
        _uiState.update {
            it.copy(sideA = it.sideA.copy(prefix = prefix))
        }
        convertAtoB()
    }

    fun onUnitAChanged(unit: UnitItem) {
        _uiState.update {
            it.copy(sideA = it.sideA.copy(unit = unit))
        }
        convertAtoB()
    }

    fun onInputBChanged(value: String) {
        if (isUpdating) return

        _uiState.update {
            it.copy(sideB = it.sideB.copy(input = value))
        }
        convertBtoA()
    }

    fun onPrefixBChanged(prefix: SIPrefix) {
        _uiState.update {
            it.copy(sideB = it.sideB.copy(prefix = prefix))
        }
        convertBtoA()
    }

    fun onUnitBChanged(unit: UnitItem) {
        _uiState.update {
            it.copy(sideB = it.sideB.copy(unit = unit))
        }
        convertBtoA()
    }

    private fun convertAtoB() {
        val state = _uiState.value
        val input = state.sideA.input.toDoubleOrNull() ?: run {
            _uiState.update {
                it.copy(sideB = it.sideB.copy(input = ""))
            }
            return
        }

        isUpdating = true

        // 1️⃣ ไปหน่วยฐาน
        val baseValue =
            input *
                    10.0.pow(state.sideA.prefix.power) *
                    state.sideA.unit.multiplier

        // 2️⃣ จากฐาน → ปลายทาง
        val output =
            baseValue /
                    state.sideB.unit.multiplier /
                    10.0.pow(state.sideB.prefix.power)

        _uiState.update {
            it.copy(
                sideB = it.sideB.copy(input = output.toString())
            )
        }

        isUpdating = false
    }

    private fun convertBtoA() {
        val state = _uiState.value
        val input = state.sideB.input.toDoubleOrNull() ?: return

        isUpdating = true

        val baseValue =
            input *
                    10.0.pow(state.sideB.prefix.power) *
                    state.sideB.unit.multiplier

        val output =
            baseValue /
                    state.sideA.unit.multiplier /
                    10.0.pow(state.sideA.prefix.power)

        _uiState.update {
            it.copy(
                sideA = it.sideA.copy(input = output.toString())
            )
        }

        isUpdating = false
    }
}


}