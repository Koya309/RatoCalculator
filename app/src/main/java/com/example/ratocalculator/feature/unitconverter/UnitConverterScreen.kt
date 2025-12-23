package com.example.ratocalculator.feature.unitconverter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme

@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val tabs = UnitCategory.values()
    val maxHeight = 40.dp * 5

    var selectedTab by remember { mutableIntStateOf(state.category.ordinal) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {

        // =========================
        // 🔹 TAB BAR
        // =========================
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        viewModel.onCategoryChanged(category)
                    },
                    text = { Text(category.displayName) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // =========================
        // 🔹 SIDE A (ต้นทาง)
        // =========================
        ConverterRow(
            side = state.sideA,
            prefixes = allSIPrefixes,
            units = state.availableUnits,
            maxHeight = maxHeight,
            onValueChange = viewModel::onInputAChanged,
            onPrefixSelected = viewModel::onPrefixASelected,
            onUnitSelected = viewModel::onUnitASelected
        )

        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(12.dp))

        // =========================
        // 🔹 SIDE B (ปลายทาง)
        // =========================
        ConverterRow(
            side = state.sideB,
            prefixes = allSIPrefixes,
            units = state.availableUnits,
            maxHeight = maxHeight,
            onValueChange = viewModel::onInputBChanged,
            onPrefixSelected = viewModel::onPrefixBSelected,
            onUnitSelected = viewModel::onUnitBSelected
        )
    }
}

@Composable
fun ConverterRow(
    side: ConverterSideState,
    prefixes: List<SIPrefix>,
    units: List<UnitItem>,
    maxHeight: Dp,
    onValueChange: (String) -> Unit,
    onPrefixSelected: (SIPrefix) -> Unit,
    onUnitSelected: (UnitItem) -> Unit
) {
    var prefixExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔹 Input
            OutlinedTextField(
                value = side.input,
                onValueChange = { value ->
                    if (value.matches(Regex("""^\d*\.?\d*$"""))) {
                        onValueChange(value)
                    }
                },
                singleLine = true,
                modifier = Modifier.weight(3f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Spacer(Modifier.width(8.dp))

            // 🔹 Prefix Dropdown
            Box {
                OutlinedButton(onClick = { prefixExpanded = true }) {
                    Text(side.prefix.symbol.ifBlank { "—" })
                }

                DropdownMenu(
                    expanded = prefixExpanded,
                    onDismissRequest = { prefixExpanded = false },
                    modifier = Modifier.heightIn(max = maxHeight)
                ) {
                    prefixes.forEach { prefix ->
                        DropdownMenuItem(
                            text = {
                                if (prefix.symbol.isBlank())
                                    Text("— (no prefix)")
                                else
                                    Text("${prefix.symbol}  ${prefix.name}  10${toSuperscript(prefix.power)}")
                            },
                            onClick = {
                                onPrefixSelected(prefix)
                                prefixExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // 🔹 Unit Dropdown
            Box {
                OutlinedButton(onClick = { unitExpanded = true }) {
                    Text(side.unit.symbol)
                }

                DropdownMenu(
                    expanded = unitExpanded,
                    onDismissRequest = { unitExpanded = false }
                ) {
                    units.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text("${unit.symbol} (${unit.name})") },
                            onClick = {
                                onUnitSelected(unit)
                                unitExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        // 🔹 Text แสดงผลแบบเต็ม
        Text(
            text = buildString {
                append(side.input.ifBlank { "0" })
                append(" ")
                append(side.prefix.name)
                append(" ")
                append(side.unit.name)
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        UnitConverterScreen()
    }
}