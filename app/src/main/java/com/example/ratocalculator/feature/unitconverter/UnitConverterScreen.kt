package com.example.ratocalculator.feature.unitconverter

import android.R.id.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme


@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val tabs = UnitCategory.values()
    var selectedTab by remember { mutableStateOf(uiState.category.ordinal) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        viewModel.updateCategory(category)
                    },
                    text = { Text(category.name) }
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            InputSection(
                inputValue = uiState.input,
                selectedUnit = uiState.fromUnit,
                onUnitChange = { viewModel.updateFromUnit(it) }
            )

            Spacer(Modifier.height(8.dp))

            SwapButton(onSwap = { viewModel.swapUnits() })

            Spacer(Modifier.height(8.dp))

            OutputSection(
                outputValue = uiState.output,
                selectedUnit = uiState.toUnit,
                onUnitChange = { viewModel.updateToUnit(it) }
            )

            Spacer(Modifier.height(16.dp))

            NumberPad(onNumberClick = { viewModel.onNumberClick(it) })
        }
    }
}

@Composable
fun InputSection(inputValue: String, selectedUnit: String, onUnitChange: (String) -> Unit) {
    Column {
        Text("Input: $inputValue")
        Text("From Unit: $selectedUnit")
    }
}

@Composable
fun OutputSection(outputValue: String, selectedUnit: String, onUnitChange: (String) -> Unit) {
    Column {
        Text("Output: $outputValue")
        Text("To Unit: $selectedUnit")
    }
}

@Composable
fun SwapButton(onSwap: () -> Unit) {
    Button(onClick = onSwap) {
        Text("Swap Units")
    }
}

@Composable
fun NumberPad(onNumberClick: (String) -> Unit) {
    Row {
        (0..9).forEach {
            Button(onClick = { onNumberClick(it.toString()) }) {
                Text(it.toString())
            }
        }
    }
}

@Composable
fun DropdownMenuDemo(x0: String, x1: (String) -> Unit) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        UnitConverterScreen()
    }
}