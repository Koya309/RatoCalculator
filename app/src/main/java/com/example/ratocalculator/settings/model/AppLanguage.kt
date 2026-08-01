package com.example.ratocalculator.settings.model

import androidx.annotation.StringRes
import com.example.ratocalculator.R

enum class AppLanguage(
    @StringRes val labelResId: Int
){
    SYSTEM(R.string.system_default),
    ENGLISH(R.string.language_english),
    THAI(R.string.language_thai)
}