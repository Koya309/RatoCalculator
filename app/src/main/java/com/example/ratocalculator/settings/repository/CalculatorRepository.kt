package com.example.ratocalculator.settings.repository

import com.example.ratocalculator.data.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CalculatorRepository @Inject constructor(
    private val repository: SettingRepository // Hilt immediately plugged the data storage device directly into the system.
){
    // Export as a plain, raw flow (no stateIn required).
    val decimalPlaces: Flow<Int> = repository.decimalPlaces
    val removeTrailingZero: Flow<Boolean> = repository.removeTrailingZero

    // The function saves values (change it to a suspend function so that the ViewModel side can call it in the routine).
    suspend fun saveDecimalPlaces(value: Int) {
        repository.saveDecimalPlaces(value)
    }

    suspend fun saveRemoveTrailingZero(value: Boolean) {
        repository.saveRemoveTrailingZero(value)
    }

    suspend fun resetToDefault() {
        repository.saveDecimalPlaces(2)
        repository.saveRemoveTrailingZero(true)
    }
}