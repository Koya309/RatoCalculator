package com.example.ratocalculator.data

import android.R.attr.value
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import jakarta.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings_pref")

class SettingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    //Everything is included in one place, and it's private for security
    private val DECIMAL_PLACES_KEY = intPreferencesKey(name = "decimal_places")
    private val TRAILING_ZERO_KEY = booleanPreferencesKey(name = "remove_trailing_zero")
    private val SIDE_BLOCK_KEY = booleanPreferencesKey(name = "side_block")
    private val ADVANCED_PREFIX_KEY = booleanPreferencesKey(name = "advanced_prefix")


    // ─── 1.Basic Calculator ───
    // The values are extracted and passed (Flow) to the ViewModel fore further use
    val decimalPlaces: Flow<Int> = context.dataStore.data.map { pref ->
        pref[DECIMAL_PLACES_KEY] ?: 2
    }

    val removeTrailingZero: Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[TRAILING_ZERO_KEY] ?: true
    }

    // suspend fun to comply with the Kotlin rules
    suspend fun saveDecimalPlaces(value: Int) {
        context.dataStore.edit { pref ->
            pref[DECIMAL_PLACES_KEY] = value
        }
    }

    suspend fun saveRemoveTrailingZero(value: Boolean) {
        context.dataStore.edit { pref ->
            pref[TRAILING_ZERO_KEY] = value
        }
    }

    // ─── 2.Unit Converter ───

    val sideBlock: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SIDE_BLOCK_KEY] ?: false
        }

    val isAdvancedPrefixEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ADVANCED_PREFIX_KEY] ?: false
    }

    suspend fun saveSideBlock(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[SIDE_BLOCK_KEY] = value
        }
    }

    suspend fun saveAdvancedPrefixSetting(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[ADVANCED_PREFIX_KEY] = value
        }
    }
}
