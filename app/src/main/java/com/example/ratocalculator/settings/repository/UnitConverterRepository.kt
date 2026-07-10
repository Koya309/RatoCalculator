package com.example.ratocalculator.settings.repository

import androidx.datastore.preferences.protobuf.Value
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnitConverterRepository @Inject constructor(
    private val repository: UnitConverterRepository
){
    val sideBlock: Flow<Boolean> = repository.sideBlock

    suspend fun saveSideBlock(value: Boolean){
        repository.saveSideBlock(value)
    }
}