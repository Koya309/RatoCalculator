package com.example.ratocalculator

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import com.example.ratocalculator.settings.SettingsViewModel
import com.example.ratocalculator.settings.model.AppLanguage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() { //AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val appTheme by settingsViewModel.appTheme.collectAsStateWithLifecycle()
            val appLanguage by settingsViewModel.appLanguage.collectAsStateWithLifecycle()

            LaunchedEffect(appLanguage) {
                updateAppLanguage(appLanguage)
            }

            RatoCalculatorTheme(appTheme = appTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RatoCalculator()
                }
            }
        }
    }

    private fun updateAppLanguage(language: AppLanguage) {
        val localeTag = when (language) {
            AppLanguage.SYSTEM -> ""
            AppLanguage.ENGLISH -> "en"
            AppLanguage.THAI -> "th"
        }

        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val targetLocale = LocaleListCompat.forLanguageTags(localeTag)

        if (currentLocales.toLanguageTags() != targetLocale.toLanguageTags()) {
            AppCompatDelegate.setApplicationLocales(targetLocale)
        }
    }
}