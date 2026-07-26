package com.example.ratocalculator.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ratocalculator.feature.unitconverter.UnitDisplayMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import jakarta.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings_pref")

class SettingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //Everything is included in one place, and it's private for security
    // ─── 1.Basic Calculator ───
    private val DECIMAL_PLACES_KEY = intPreferencesKey(name = "decimal_places")
    private val TRAILING_ZERO_KEY = booleanPreferencesKey(name = "remove_trailing_zero")

    // ─── 2.Unit Converter ───
    private val SIDE_BLOCK_KEY = booleanPreferencesKey(name = "side_block")
    private val ADVANCED_PREFIX_KEY = booleanPreferencesKey(name = "advanced_prefix")
    private val UNIT_DISPLAY_MODE = stringPreferencesKey(name = "unit_display_mode")

    // ─── 2-1. Save Unit Converter
    private val LAST_CATEGORY_KEY = stringPreferencesKey(name = "last_category")
    private val LAST_PREFIX_A_KEY = stringPreferencesKey(name = "last_prefix_a")
    private val LAST_UNIT_A_KEY = stringPreferencesKey(name = "last_unit_a")
    private val LAST_PREFIX_B_KEY = stringPreferencesKey(name = "last_prefix_b")
    private val LAST_UNIT_B_KEY = stringPreferencesKey(name = "last_unit_b")



    // ─── 1.Basic Calculator ───
    // The values are extracted and passed (Flow) to the ViewModel fore further use
    val decimalPlaces: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[DECIMAL_PLACES_KEY] ?: 2
    }

    val removeTrailingZero: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[TRAILING_ZERO_KEY] ?: true
    }

    // suspend fun to comply with the Kotlin rules
    suspend fun saveDecimalPlaces(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[DECIMAL_PLACES_KEY] = value
        }
    }

    suspend fun saveRemoveTrailingZero(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TRAILING_ZERO_KEY] = value
        }
    }

    // ─── 2.Unit Converter ───

    val sideBlock: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SIDE_BLOCK_KEY] ?: false
        }

    val AdvancedPrefix: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ADVANCED_PREFIX_KEY] ?: false
    }

    val unitDisplayMode: Flow<UnitDisplayMode> = context.dataStore.data
        .map { preferences ->
            val savedName = preferences[UNIT_DISPLAY_MODE]
                ?: UnitDisplayMode.FULL_NAME.name

            // to String ->  Enum Object (Protect App Crash If conversion is not possible.)
            try {
                UnitDisplayMode.valueOf(savedName)
            } catch (_: IllegalArgumentException) {
                UnitDisplayMode.FULL_NAME
            }
        }

    suspend fun saveSideBlock(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[SIDE_BLOCK_KEY] = value
        }
    }

    suspend fun saveAdvancedPrefix(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[ADVANCED_PREFIX_KEY] = value
        }
    }

    suspend fun saveUnitDisplayMode(mode: UnitDisplayMode) {
        context.dataStore.edit { preferences ->
            // save Enum (.name) in DataStore such PREFIX, POWER, FULL_NAME
            preferences[UNIT_DISPLAY_MODE] = mode.name
        }
    }

    data class SavedConverterState(
        val category: String,
        val prefixA: String,
        val unitA: String,
        val prefixB: String,
        val unitB: String
    )

    val savedConverterState: Flow<SavedConverterState> = context.dataStore.data.map { preferences ->
        SavedConverterState(
            category = preferences[LAST_CATEGORY_KEY] ?: "Mass",
            prefixA = preferences[LAST_PREFIX_A_KEY] ?: "",
            unitA = preferences[LAST_UNIT_A_KEY] ?: "",
            prefixB = preferences[LAST_PREFIX_B_KEY] ?: "",
            unitB = preferences[LAST_UNIT_B_KEY] ?: ""
        )
    }

    suspend fun saveConverterPreferences(
        category: String? = null,
        prefixA: String? = null,
        unitA: String? = null,
        prefixB: String? = null,
        unitB: String? = null
    ) {
        context.dataStore.edit { preferences ->
            category?.let { preferences[LAST_CATEGORY_KEY] = it }
            prefixA?.let { preferences[LAST_PREFIX_A_KEY] = it }
            unitA?.let { preferences[LAST_UNIT_A_KEY] = it }
            prefixB?.let { preferences[LAST_PREFIX_B_KEY] = it }
            unitB?.let { preferences[LAST_UNIT_B_KEY] = it }
        }
    }
}
