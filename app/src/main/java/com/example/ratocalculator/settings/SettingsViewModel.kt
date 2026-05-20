package com.example.ratocalculator.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratocalculator.data.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingRepository // Hilti immediately plugged the data storage device directly into the system.
) : ViewModel() {

    // Convert raw data streams into a state on the Compose screen, ready to be viewed at any time.
    val decimalPlaces = repository.decimalPlaces
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2)

    val removeTrailingZero = repository.removeTrailingZero
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // 1. Function when the user adjusts the decimal places.
    fun updateDecimalPlaces(value: Int) {
        viewModelScope.launch { repository.saveDecimalPlaces(value) }
    }

    // 2. Function when the user flips the switch to delete the last zero.
    fun updateRemoveTrailingZero(value: Boolean) {
        viewModelScope.launch { repository.saveRemoveTrailingZero(value) }
    }

    // 3. The system reset function you need!! Restores to default settings and overwrites saves immediately.
    fun resetToDefault() {
        viewModelScope.launch {
            repository.saveDecimalPlaces(2)       // คืนค่ากลับเป็นทศนิยม 2 ตำแหน่ง
            repository.saveRemoveTrailingZero(true) // คืนค่ากลับเป็นเปิดการลบเลขศูนย์ท้าย
        }
    }
}