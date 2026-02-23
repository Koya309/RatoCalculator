package com.example.ratocalculator.feature.unitconverter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow
import com.example.ratocalculator.feature.unitconverter.toSuperscript

@HiltViewModel
class UnitConverterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(UnitConverterUiState())
    val uiState = _uiState.asStateFlow()

    private var isUpdating = false

    fun onCategoryChanged(category: UnitCategory) {
        val units = when (category) {
            UnitCategory.Mass -> massUnits
            UnitCategory.Length -> lengthUnits
            UnitCategory.Area -> areaUnits
            UnitCategory.Temperature -> temperatureUnits
            UnitCategory.Volume -> volumeUnits
            else -> emptyList()
        }

        _uiState.update {
            it.copy(
                category = category,
                availableUnits = units,
                sideA = it.sideA.copy(unit = units.first(), input = ""),
                sideB = it.sideB.copy(unit = units.getOrNull(1) ?: units.first(), input = "")
            )
        }
    }

    fun onInputAChanged(value: String) {
        if (isUpdating) return
        _uiState.update { it.copy(sideA = it.sideA.copy(input = value)) }
        performConversion(fromA = true)
    }

    fun onInputBChanged(value: String) {
        if (isUpdating) return
        _uiState.update { it.copy(sideB = it.sideB.copy(input = value)) }
        performConversion(fromA = true)
    }

    fun onUnitAChanged(unit: UnitItem) {
        _uiState.update { it.copy(sideA = it.sideA.copy(unit = unit)) }
        performConversion(fromA = true)
    }

    fun onUnitBChanged(unit: UnitItem) {
        _uiState.update { it.copy(sideB = it.sideB.copy(unit = unit)) }
        performConversion(fromA = false)
    }

    fun onPrefixAChanged(prefix: SIPrefix) {
        _uiState.update { it.copy(sideA = it.sideA.copy(prefix = prefix)) }
        performConversion(fromA = true)
    }

    fun onPrefixBChanged(prefix: SIPrefix) {
        _uiState.update { it.copy(sideB = it.sideB.copy(prefix = prefix)) }
        performConversion(fromA = false)
    }

    private fun performConversion(fromA: Boolean) {
        val state = _uiState.value
        val sourceSide = if (fromA) state.sideA else state.sideB
        val targetSide = if (fromA) state.sideB else state.sideA

        // ลองแปลง String เป็น Double ถ้าไม่ได้ให้ล้างค่าฝั่งตรงข้าม
        val inputDouble = sourceSide.input.toDoubleOrNull()
        if (inputDouble == null) {
            updateTargetInput(fromA, "")
            return
        }

        isUpdating = true

        // 1. คำนวณค่าผลลัพธ์ (Logic: Input -> Base -> Output)
        val result = calculateConversion(inputDouble, sourceSide, targetSide)

        // 2. จัดรูปแบบการแสดงผล (Normal หรือ Power of Ten)
        val formattedResult = formatDisplayValue(result)

        updateTargetInput(fromA, formattedResult)

        isUpdating = false
    }

    private fun calculateConversion(input: Double, from: SideState, to: SideState): Double {
        // ขั้นตอนที่ 1: แปลงจากหน่วยต้นทางเข้าสู่หน่วยฐาน (Base Unit)
        // (ค่า x 10^prefixPower) แล้วส่งเข้า toBase ของหน่วยนั้นๆ
        val baseValue = from.unit.toBase(input * 10.0.pow(from.prefix.power))

        // ขั้นตอนที่ 2: แปลงจากหน่วยฐานไปยังหน่วยปลายทาง
        // (ส่งเข้า fromBase ของหน่วยนั้นๆ) แล้วหารด้วย 10^prefixPower ของปลายทาง
        return to.unit.fromBase(baseValue) / 10.0.pow(to.prefix.power)
    }

    private fun updateTargetInput(fromA: Boolean, value: String) {
        _uiState.update {
            if (fromA) it.copy(sideB = it.sideB.copy(input = value))
            else it.copy(sideA = it.sideA.copy(input = value))
        }
    }
    private fun formatDisplayValue(value: Double): String {
        if (value == 0.0) return "0"
        val absValue = abs(value)

        // ถ้าเลขอยู่ในช่วงที่อ่านง่าย (0.001 ถึง 1,000,000) ให้แสดงเลขปกติ
        return if (absValue >= 1e-3 && absValue < 1e6) {
            "%.6f".format(value).trimEnd('0').trimEnd('.')
        } else {
            formatToPowerOfTen(value)
        }
    }

    private fun formatToPowerOfTen(value: Double): String {
        val s = "%.4E".format(value)
        val parts = s.split("E")
        if (parts.size < 2) return s

        val mantissa = parts[0].trimEnd('0').trimEnd('.')
        val exponent = parts[1].replace("+", "").toInt()

        if (exponent == 0) return mantissa

        val superExponent = toSuperscript(exponent)
        return "$mantissa × 10$superExponent"
    }

}