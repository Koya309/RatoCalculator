package com.example.ratocalculator.feature.unitconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratocalculator.core.toSuperscript
import com.example.ratocalculator.settings.repository.UnitConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.pow

@HiltViewModel
class UnitConverterViewModel @Inject constructor(
    private val unitConverterRepo: UnitConverterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UnitConverterUiState())
    val uiState = _uiState.asStateFlow()

    val isSideBLocked = unitConverterRepo.sideBlock
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    private var isUpdating = false

    init {
        viewModelScope.launch {
            unitConverterRepo.savedConverterState.collect { saved ->
                val currentCategory = try {
                    UnitCategory.valueOf(saved.category)
                } catch (e: Exception){
                    UnitCategory.Mass
                }
                val units = when (currentCategory){
                    UnitCategory.Mass -> massUnits
                    UnitCategory.Length -> lengthUnits
                    UnitCategory.Area -> areaUnits
                    UnitCategory.Temperature -> temperatureUnits
                    UnitCategory.Volume -> volumeUnits
                }
                val selectedUnitA = units.find { it.symbol == saved.unitA } ?: units.first()
                val selectedPrefixA = allSIPrefixes.find { it.symbol == saved.prefixA } ?: allSIPrefixes.find { it.power == 0 } ?: SIPrefix(" ", "", 0)

                val selectedUnitB = units.find { it.symbol == saved.unitB } ?: units.getOrNull(1) ?: units.first()
                val selectedPrefixB = allSIPrefixes.find { it.symbol == saved.prefixB } ?: allSIPrefixes.find { it.power == 0 } ?: SIPrefix(" ", "", 0)

                _uiState.update {
                    it.copy(
                        category = currentCategory,
                        availableUnits = units,
                        sideA = it.sideA.copy(prefix = selectedPrefixA, unit = selectedUnitA),
                        sideB = it.sideB.copy(prefix = selectedPrefixB, unit = selectedUnitB)
                    )
                }
            }
        }
    }

    val isAdvancedEnabled : StateFlow<Boolean> = unitConverterRepo.AdvancedPrefix
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val visibleSIPrefixes : StateFlow<List<SIPrefix>> = isAdvancedEnabled
        .map { advanced ->
            if (advanced){
                allSIPrefixes
            } else {
                allSIPrefixes.filter { prefix ->
                    prefix.power in -3..9
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = allSIPrefixes.filter { it.power in -3..9 }
        )

    fun onCategoryChanged(category: UnitCategory) {
        val units = when (category) {
            UnitCategory.Mass -> massUnits
            UnitCategory.Length -> lengthUnits
            UnitCategory.Area -> areaUnits
            UnitCategory.Temperature -> temperatureUnits
            UnitCategory.Volume -> volumeUnits
        }
        val defaultUnitA = units.first()
        val defaultUnitB = units.getOrNull(1) ?: units.first()

        _uiState.update {
            it.copy(
                category = category,
                availableUnits = units,
                sideA = it.sideA.copy(unit = defaultUnitA, input = ""),
                sideB = it.sideB.copy(unit = defaultUnitB, input = "")
            )
        }
        viewModelScope.launch {
            unitConverterRepo.saveConverterPreferences(
                category = category.name,
                unitA = defaultUnitA.symbol,
                unitB = defaultUnitB.symbol
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
        if (isSideBLocked.value) return
        _uiState.update { it.copy(sideB = it.sideB.copy(input = value)) }
        performConversion(fromA = false)
    }

    fun onUnitAChanged(unit: UnitItem) {
        _uiState.update { it.copy(sideA = it.sideA.copy(unit = unit)) }
        performConversion(fromA = true)

        viewModelScope.launch {
            unitConverterRepo.saveConverterPreferences(unitA = unit.symbol)
        }
    }

    fun onUnitBChanged(unit: UnitItem) {
        _uiState.update { it.copy(sideB = it.sideB.copy(unit = unit)) }
        performConversion(fromA = true)

        viewModelScope.launch {
            unitConverterRepo.saveConverterPreferences(unitB = unit.symbol)
        }
    }

    fun onPrefixAChanged(prefix: SIPrefix) {
        _uiState.update { it.copy(sideA = it.sideA.copy(prefix = prefix)) }
        performConversion(fromA = true)

        viewModelScope.launch {
            unitConverterRepo.saveConverterPreferences(prefixA = prefix.symbol)
        }
    }

    fun onPrefixBChanged(prefix: SIPrefix) {
        _uiState.update { it.copy(sideB = it.sideB.copy(prefix = prefix)) }

        if (isSideBLocked.value){
            performConversion(fromA = true)
        }else {
            performConversion(fromA = false)
        }
        viewModelScope.launch {
            unitConverterRepo.saveConverterPreferences(prefixB = prefix.symbol)
        }
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

        // 1. Show Result (Logic: Input -> Base -> Output)
        val result = calculateConversion(inputDouble, sourceSide, targetSide)

        // 2. Logic Display Result (Normal or Power of Ten)
        val formattedResult = formatDisplayValue(result)

        updateTargetInput(fromA, formattedResult)

        isUpdating = false
    }

    private fun calculateConversion(input: Double, from: SideState, to: SideState): Double {
        // Step 1: Convert from source unit to base unit
        // (x 10^prefixPower value) and send to the toBase of that unit.
        val baseValue = from.unit.toBase(input * 10.0.pow(from.prefix.power))

        // Step 2: Convert from base unit to destination unit
        // (Pass to the base unit's `fromBase`) and divide by 10^prefixPower of the destination unit.
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

        // If the number is within a readable range (0.001 to 1,000,000), display the normal number.
        return if (absValue in 1e-3..<1e6) {
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

    private fun generateCopyFormats(
        value: String,
        symbol: String,
        fullName: String,
        powerText: String
    ): CopyFormats{
        if (value.isBlank()) return CopyFormats()

        return CopyFormats(
            prefixUnit = "$value $symbol",
            powerUnit = "$value $powerText",
            fullNameUnit = "$value $fullName"
        )
    }

}