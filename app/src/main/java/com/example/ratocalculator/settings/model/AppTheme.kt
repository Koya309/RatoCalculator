package com.example.ratocalculator.settings.model

import androidx.annotation.StringRes
import com.example.ratocalculator.R

enum class AppTheme(
    @StringRes val labelResId: Int
){
    SYSTEM(R.string.system_default),
    LIGHT(R.string.light),
    DARK(R.string.dark)
}