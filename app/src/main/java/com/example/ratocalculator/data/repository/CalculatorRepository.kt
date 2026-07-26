package com.example.ratocalculator.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.calculatorDataStore by preferencesDataStore(name = "calculator_pref")

@Singleton
class CalculatorRepository @Inject constructor(
    @ApplicationContext private val context: Context
// Hilt immediately plugged the data storage device directly into the system.
){
    private val DECIMAL_PLACES_KEY = intPreferencesKey("decimal_places")
    private val TRAILING_ZERO_KEY = booleanPreferencesKey("remove_trailing_zero")

    // Export as a plain, raw flow (no stateIn required).
    val decimalPlaces: Flow<Int> = context.calculatorDataStore.data
        .map { preferences -> preferences[DECIMAL_PLACES_KEY] ?: 2 }

    val removeTrailingZero: Flow<Boolean> = context.calculatorDataStore.data
        .map { preferences -> preferences[TRAILING_ZERO_KEY] ?: true }

    // The function saves values (change it to a suspend function so that the ViewModel side can call it in the routine).
    suspend fun saveDecimalPlaces(value: Int) {
        context.calculatorDataStore.edit { preferences -> preferences[DECIMAL_PLACES_KEY] = value }
    }

    suspend fun saveRemoveTrailingZero(value: Boolean) {
        context.calculatorDataStore.edit { preferences -> preferences[TRAILING_ZERO_KEY] = value }
    }
}