package com.example.ratocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RatoCalculatorTheme {
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