package com.example.ratocalculator.feature.unitconverter

import android.R.id.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("แท็บ 1", "แท็บ 2", "แท็บ 3")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                Column {
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
            1 -> Text("นี่คือเนื้อหาของแท็บ 2")
            2 -> Text("นี่คือเนื้อหาของแท็บ 3")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        UnitConverterScreen()
    }
}