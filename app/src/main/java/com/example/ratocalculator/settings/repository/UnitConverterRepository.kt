package com.example.ratocalculator.settings.repository

import com.example.ratocalculator.data.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnitConverterRepository @Inject constructor(
    private val repository: SettingRepository
){
    val sideBlock: Flow<Boolean> = repository.sideBlock

    val isAdvancedPrefixEnabled: Flow<Boolean> = repository.isAdvancedPrefixEnabled

    suspend fun saveSideBlock(value: Boolean){
        repository.saveSideBlock(value)
    }

    suspend fun saveAdvancedPrefixEnabled(value: Boolean){
        repository.saveAdvancedPrefixSetting(value)
    }
}