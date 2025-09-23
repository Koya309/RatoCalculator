package com.example.ratocalculator.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = hiltViewModel()
    val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)

    Column(
        modifier = Modifier.padding(all = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "ตัด .0 ถ้าเป็นจำนวนเต็ม")
            Switch(
                checked = removeTrailingZero,
                onCheckedChange = { viewModel.updateRemoveTrailingZero(it) }
            )
        }
        Spacer(Modifier.height(16.dp))

        Text(text = "จำนวนทศนิยม")

        Slider(
            value = decimalPlaces.toFloat(),
            onValueChange = { viewModel.updateDecimalPlaces(it.toInt()) },
            valueRange = 0f..5f,
            steps = 5
        )

        Text(text = "แสดง ${decimalPlaces} ตำแหน่ง")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        SettingsScreen()
    }
}