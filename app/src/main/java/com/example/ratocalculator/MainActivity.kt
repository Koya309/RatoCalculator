package com.example.ratocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import com.example.ratocalculator.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val appTheme by settingsViewModel.appTheme.collectAsState()

            RatoCalculatorTheme(appTheme = appTheme) {
                RatoCalculator()

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
    }
}