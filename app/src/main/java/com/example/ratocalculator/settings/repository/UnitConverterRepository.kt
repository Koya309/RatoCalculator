package com.example.ratocalculator.settings.repository

import com.example.ratocalculator.data.SettingRepository
import com.example.ratocalculator.data.SettingRepository.SavedConverterState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnitConverterRepository @Inject constructor(
    private val repository: SettingRepository
){
    val sideBlock: Flow<Boolean> = repository.sideBlock

    val AdvancedPrefix: Flow<Boolean> = repository.AdvancedPrefix

    val savedConverterState: Flow<SavedConverterState> = repository.savedConverterState

    suspend fun saveSideBlock(value: Boolean){
        repository.saveSideBlock(value)
    }

    suspend fun saveAdvancedPrefix(value: Boolean){
        repository.saveAdvancedPrefix(value)
    }

    suspend fun saveConverterPreferences(
        category: String? = null,
        prefixA: String? = null,
        unitA: String? = null,
        prefixB: String? = null,
        unitB: String? = null
    ) {
        repository.saveConverterPreferences(
            category = category,
            prefixA = prefixA,
            unitA = unitA,
            prefixB = prefixB,
            unitB = unitB
        )
    }
}