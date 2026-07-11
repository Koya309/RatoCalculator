package com.example.ratocalculator.settings.repository

import com.example.ratocalculator.data.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnitConverterRepository @Inject constructor(
    private val repository: SettingRepository
){
    val sideBlock: Flow<Boolean> = repository.sideBlock

    suspend fun saveSideBlock(value: Boolean){
        repository.saveSideBlock(value)
    }
}