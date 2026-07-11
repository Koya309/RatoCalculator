package com.example.ratocalculator.data

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
    // ─── 1.Basic Calculator ───

    // ตั้งชื่อกุญแจสำหรับล็อกช่องเก็บข้อมูลตามแบบในกระดาษของคุณ
    private val DECIMAL_PLACES_KEY = intPreferencesKey(name = "decimal_places")
    private val TRAILING_ZERO_KEY = booleanPreferencesKey(name = "remove_trailing_zero")

    // ดึงค่าออกมาส่งต่อเป็นท่อน้ำ (Flow) ให้ ViewModel นำไปใช้ต่อ
    val decimalPlaces: Flow<Int> = context.dataStore.data.map { pref ->
        pref[DECIMAL_PLACES_KEY] ?: 2
    }

    val removeTrailingZero: Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[TRAILING_ZERO_KEY] ?: true
    }

    // เอาคำว่า async ออก เหลือแค่ suspend fun เพื่อให้ถูกต้องตามหลักของ Kotlin
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

    suspend fun saveSideBlock(value: Boolean){
        context.dataStore.edit { preferences ->
            preferences[SIDE_BLOCK_KEY] = value
        }
    }

    companion object {
        val SIDE_BLOCK_KEY = booleanPreferencesKey("side_block")
    }


}
