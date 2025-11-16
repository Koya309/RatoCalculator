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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme

@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel = hiltViewModel()
) {
//    val uiState by viewModel.uiState.collectAsState()
    val tabs = UnitCategory.values()
    val maxHeight = 40.dp * 5
//    var selectedTab by remember { mutableStateOf(uiState.category.ordinal) }
//    var kgInput by remember { mutableStateOf("7") }
    val state by viewModel.uiState.collectAsState()
//    val kgValue = (kgInput.toDoubleOrNull() ?: 0.0)
//    val gramValue = kgValue * 1000
    var prefixExpanded by remember { mutableStateOf(false) }
//    var unitExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, category ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        viewModel.updateCategory(category)
                    },
                    text = { Text(category.displayName) }
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            // 🔹 Input Value
            OutlinedTextField(
                value = state.inputValue,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("""^\d*\.?\d*$"""))) {
                        viewModel.onInputChanged(newValue, state.selectedPrefix)
                    }
                },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* ปิดคีย์บอร์ดหรือทำ action อื่น */ }
                )
            )

            Spacer(Modifier.width(8.dp))

            // 🔹 A = Dropdown SIPrefix
            Box {
                OutlinedButton(onClick = { prefixExpanded = true }) {
                    Text(
                        if (state.selectedPrefix.symbol.isBlank()) ""
                        else  "${state.selectedPrefix.symbol}"

                    )
                }
                DropdownMenu(
                    expanded = prefixExpanded,
                    onDismissRequest = { prefixExpanded = false },
                    modifier = Modifier.heightIn(max = maxHeight)
                ) {
                    state.prefixes.forEach { prefix ->
                        DropdownMenuItem(
                            text = {
                                val symbolText =
                                    if (prefix.symbol.isBlank()) ""
                                    else "${prefix.symbol}    ${prefix.name}     10${viewModel.toSuperscript(prefix.power)}"
                                Text(symbolText)
                            },
                            onClick = {
                                viewModel.onPrefixSelected(prefix)
                                prefixExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            // 🔹 B = Dropdown SI Unit (ขึ้นกับ Tab / Category)
            var unitExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { unitExpanded = true }) {
                    Text(state.selectedUnit.symbol)
                }
                DropdownMenu(
                    expanded = unitExpanded,
                    onDismissRequest = { unitExpanded = false }
                ) {
                    state.availableUnits.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text("${unit.symbol} (${unit.name})") },
                            onClick = {
                                viewModel.onUnitSelected(unit)
                                unitExpanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))

        // 🔹 แสดงค่าที่กรอก + prefix + unit
        Text(
            text = buildString {
                append(state.inputValue.toDoubleOrNull() ?: 0.0)
                append(" ")
                append(state.selectedPrefix.name)
                append(state.selectedUnit.name)
            },
            style = MaterialTheme.typography.titleMedium
        )
    //    --->
        // swapButtom()

    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        UnitConverterScreen()
    }
}