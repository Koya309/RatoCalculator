package com.example.ratocalculator.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ratocalculator.feature.unitconverter.UnitDisplayMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.unitConverterDataStore by preferencesDataStore(name = "unit_converter_pref")

@Singleton
class UnitConverterRepository @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val SIDE_BLOCK_KEY = booleanPreferencesKey("side_block")
    private val ADVANCED_PREFIX_KEY = booleanPreferencesKey("advanced_prefix")
    private val UNIT_DISPLAY_MODE = stringPreferencesKey("unit_display_mode")

    private val LAST_CATEGORY_KEY = stringPreferencesKey("last_category")
    private val LAST_PREFIX_A_KEY = stringPreferencesKey("last_prefix_a")
    private val LAST_UNIT_A_KEY = stringPreferencesKey("last_unit_a")
    private val LAST_PREFIX_B_KEY = stringPreferencesKey("last_prefix_b")
    private val LAST_UNIT_B_KEY = stringPreferencesKey("last_unit_b")

    val sideBlock: Flow<Boolean> = context.unitConverterDataStore.data
        .map { preferences -> preferences[SIDE_BLOCK_KEY] ?: false }

    val AdvancedPrefix: Flow<Boolean> = context.unitConverterDataStore.data
        .map { preferences -> preferences[ADVANCED_PREFIX_KEY] ?: false }

    val unitDisplayMode: Flow<UnitDisplayMode> = context.unitConverterDataStore.data
        .map { preferences ->
            val savedName = preferences[UNIT_DISPLAY_MODE] ?: UnitDisplayMode.FULL_NAME.name
            try {
                UnitDisplayMode.valueOf(savedName)
            } catch (_: IllegalArgumentException) {
                UnitDisplayMode.FULL_NAME
            }
        }

    suspend fun saveSideBlock(value: Boolean) {
        context.unitConverterDataStore.edit { preferences -> preferences[SIDE_BLOCK_KEY] = value }
    }

    suspend fun saveAdvancedPrefix(value: Boolean) {
        context.unitConverterDataStore.edit { preferences -> preferences[ADVANCED_PREFIX_KEY] = value }
    }

    suspend fun saveUnitDisplayMode(mode: UnitDisplayMode) {
        context.unitConverterDataStore.edit { preferences -> preferences[UNIT_DISPLAY_MODE] = mode.name }
    }

    data class SavedConverterState(
        val category: String,
        val prefixA: String,
        val unitA: String,
        val prefixB: String,
        val unitB: String
    )

    val savedConverterState: Flow<SavedConverterState> = context.unitConverterDataStore.data.map { preferences ->
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
        context.unitConverterDataStore.edit { preferences ->
            category?.let { preferences[LAST_CATEGORY_KEY] = it }
            prefixA?.let { preferences[LAST_PREFIX_A_KEY] = it }
            unitA?.let { preferences[LAST_UNIT_A_KEY] = it }
            prefixB?.let { preferences[LAST_PREFIX_B_KEY] = it }
            unitB?.let { preferences[LAST_UNIT_B_KEY] = it }
        }
    }
}