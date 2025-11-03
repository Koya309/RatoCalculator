package com.example.ratocalculator.feature.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.settings.SettingsViewModel
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme

@Composable
fun CalculatorScreen(
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    // Pull value SettingsViewModel
    val decimalPlaces by settingsViewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by settingsViewModel.removeTrailingZero.collectAsState(initial = true)

    // pull uiState from CalculatorViewModel
    val uiState by calculatorViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
        //    .background(Color(0xFF101820))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF1C1C1E), RoundedCornerShape(16.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = uiState.input.ifEmpty { "0" },
                fontSize = 28.sp,
                color = Color.LightGray,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiState.result.ifEmpty { "0" },
                fontSize = 40.sp,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val buttons = listOf(
            listOf("C", "", "x10", "DEL"),
            listOf("7", "8", "9", "÷"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+")
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { label ->
                        CalculatorButton(
                            label = label,
                            modifier = Modifier
                                .weight(if (label == "0" && row.size == 1) 4f else 4f)
                                .aspectRatio(1f),
                            onClick = {
                                calculatorViewModel.onButtonClick(
                                    label = label,
                                    decimalPlaces = decimalPlaces,
                                    removeTrailingZero = removeTrailingZero
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (label in listOf("÷", "×", "-", "+", "="))
                colorResource(id = R.color.clear_button)
            else
                colorResource(id = R.color.operator_button),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (label == "DEL" || label == "backspace") {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "Backspace",
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = label,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RatoCalculatorApp() {
    RatoCalculatorTheme {
        CalculatorScreen()
    }
}