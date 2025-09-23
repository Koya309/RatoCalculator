package com.example.ratocalculator.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

        private val _decimalPlaces = MutableStateFlow(2)
        val decimalPlaces: StateFlow<Int> = _decimalPlaces
        private val _removeTrailingZero = MutableStateFlow(true)
        val removeTrailingZero: StateFlow<Boolean> = _removeTrailingZero

        fun updateDecimalPlaces(value: Int) {
            _decimalPlaces.value = value
        }
        fun updateRemoveTrailingZero(value: Boolean) {
            _removeTrailingZero.value = value
        }
}