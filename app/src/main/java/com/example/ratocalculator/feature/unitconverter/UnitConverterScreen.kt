package com.example.ratocalculator.feature.unitconverter

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.core.toSuperscript
import com.example.ratocalculator.core.theme.RatoCalculatorTheme

@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isSideBLocked by viewModel.isSideBLocked.collectAsState()
    val visiblePrefixes by viewModel.visibleSIPrefixes.collectAsState()
    val tabs = UnitCategory.entries
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
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier,
            containerColor = TabRowDefaults.primaryContainerColor,
            contentColor = TabRowDefaults.primaryContentColor,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(selectedTab)
                )
            },
            @Composable { HorizontalDivider() },
            {
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
        )
        Spacer(Modifier.height(12.dp))

        // =========================
        // 🔹 SIDE A (TOP)
        // =========================
        ConverterRow(
            side = state.sideA,
            prefixes = visiblePrefixes,
            units = state.availableUnits,
            maxHeight = maxHeight,
            onValueChange = {viewModel.onInputAChanged(it)},
            onPrefixSelected = viewModel::onPrefixAChanged,
            onUnitSelected = viewModel::onUnitAChanged
        )

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        // =========================
        // 🔹 SIDE B (Bottom)
        // =========================
        ConverterRow(
            side = state.sideB,
            prefixes = visiblePrefixes,
            units = state.availableUnits,
            maxHeight = maxHeight,
            readOnly = isSideBLocked,
            onValueChange = { viewModel.onInputBChanged(it) },
            onPrefixSelected = viewModel::onPrefixBChanged,
            onUnitSelected = viewModel::onUnitBChanged
        )
    }
}

@Composable
fun ConverterRow(
    side: SideState,
    prefixes: List<SIPrefix>,
    units: List<UnitItem>,
    maxHeight: Dp,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    onPrefixSelected: (SIPrefix) -> Unit,
    onUnitSelected: (UnitItem) -> Unit
) {
    var prefixExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val inputValue = side.input.ifBlank { "0" }
    val prefixSymbol = side.prefix.symbol
    val prefixName = side.prefix.name
    val unitSymbol = side.unit.symbol
    val unitName = side.unit.name
    val powerText = "10${toSuperscript(side.prefix.power)}"

    val formats = CopyFormats(
        prefixUnit = "$inputValue $prefixSymbol$unitSymbol".trim(),
        powerUnit = "$inputValue x $powerText $unitSymbol",
        fullNameUnit = "$inputValue $prefixName $unitName".trim()
    )

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
                readOnly = readOnly,
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

        // Text Show Result + Copy Menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formats.fullNameUnit,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            CopyUnitMenu(
                formats = formats,
                onCopy = { textToCopy ->
                    clipboardManager.setText(AnnotatedString(textToCopy))
                    Toast.makeText(context, "Copied: $textToCopy", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}


@Composable
fun CopyUnitMenu(
    formats: CopyFormats,
    onCopy: (String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }

    Box{
        IconButton(onClick = {expanded = true}) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            //Prefix Unit
            DropdownMenuItem(
                text = { Text("(${formats.prefixUnit})") },
                onClick = {
                    onCopy(formats.prefixUnit)
                    expanded = false
                }
            )
            //Power Unit
            DropdownMenuItem(
                text = { Text("(${formats.powerUnit})") },
                onClick = {
                    onCopy(formats.powerUnit)
                    expanded = false
                }
            )
            //Full Name Unit
            DropdownMenuItem(
                text = { Text("(${formats.fullNameUnit})") },
                onClick = {
                    onCopy(formats.fullNameUnit)
                    expanded = false
                }
            )
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