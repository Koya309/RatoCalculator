package com.example.ratocalculator.feature.unitconverter

import android.content.ClipData
import android.content.Context
import android.content.pm.ActivityInfo
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
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.core.toSuperscript
import com.example.ratocalculator.core.utils.LockScreenOrientation
import kotlinx.coroutines.launch

@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isSideBLocked by viewModel.isSideBLocked.collectAsState()
    val visiblePrefixes by viewModel.visibleSIPrefixes.collectAsState()
    val tabs = UnitCategory.entries
    val maxHeight = 40.dp * 5

    val displayMode by viewModel.unitDisplayMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // TAB BAR
        SecondaryScrollableTabRow(
            selectedTabIndex = state.category.ordinal,
            containerColor = TabRowDefaults.primaryContainerColor,
            contentColor = TabRowDefaults.primaryContentColor,
            edgePadding = 16.dp,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(state.category.ordinal)
                )
            },
            divider = { HorizontalDivider() }
        ) {
            tabs.forEach { category ->
                Tab(
                    selected = state.category == category,
                    onClick = { viewModel.onCategoryChanged(category) },
                    text = {
                        Text(
                            text = stringResource(id = category.displayNameRes),
                            maxLines = 1
                        )
                    }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // SIDE A (TOP)
        ConverterRow(
            side = state.sideA,
            prefixes = visiblePrefixes,
            units = state.availableUnits,
            maxHeight = maxHeight,
            displayMode = displayMode,
            formatDisplayUnit = viewModel::formatDisplayUnit,
            onValueChange = { viewModel.onInputAChanged(it) },
            onPrefixSelected = viewModel::onPrefixAChanged,
            onUnitSelected = viewModel::onUnitAChanged
        )

        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(Modifier.height(12.dp))

        // SIDE B (BOTTOM)
        ConverterRow(
            side = state.sideB,
            prefixes = visiblePrefixes,
            units = state.availableUnits,
            maxHeight = maxHeight,
            readOnly = isSideBLocked,
            displayMode = displayMode,
            formatDisplayUnit = viewModel::formatDisplayUnit,
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
    displayMode: UnitDisplayMode,
    formatDisplayUnit: (Context, String, SIPrefix, UnitItem, UnitDisplayMode) -> String,
    onValueChange: (String) -> Unit,
    onPrefixSelected: (SIPrefix) -> Unit,
    onUnitSelected: (UnitItem) -> Unit
) {
    var prefixExpanded by remember { mutableStateOf(false) }
    var unitExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val inputValue = side.input.ifBlank { "0" }
    val prefixSymbol = side.prefix.symbol
    val prefixName = if (side.prefix.nameRes != 0) stringResource(id = side.prefix.nameRes) else ""
    val unitSymbol = side.unit.symbol
    val unitName = if (side.unit.nameRes != 0) stringResource(id = side.unit.nameRes) else ""
    val powerText = "10${toSuperscript(side.prefix.power)}"
    val toastFormat = stringResource(R.string.toast_copied)

    val formats = CopyFormats(
        prefixUnit = "$inputValue $prefixSymbol$unitSymbol".trim(),
        powerUnit = if (side.prefix.power != 0) "$inputValue × $powerText $unitSymbol" else "$inputValue $unitSymbol",
        fullNameUnit = "$inputValue $prefixName $unitName".trim()
    )

    val displayText = formatDisplayUnit(
        context,
        side.input,
        side.prefix,
        side.unit,
        displayMode
    )

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Input
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

            // Prefix Dropdown
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
                        val pName = if (prefix.nameRes != 0) stringResource(id = prefix.nameRes) else ""
                        DropdownMenuItem(
                            text = {
                                if (prefix.symbol.isBlank()) {
                                    Text("— (${stringResource(R.string.prefix_none)})")
                                } else {
                                    Text("${prefix.symbol}  $pName  10${toSuperscript(prefix.power)}")
                                }
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

            // Unit Dropdown
            Box {
                OutlinedButton(onClick = { unitExpanded = true }) {
                    Text(side.unit.symbol)
                }

                DropdownMenu(
                    expanded = unitExpanded,
                    onDismissRequest = { unitExpanded = false }
                ) {
                    units.forEach { unit ->
                        val uName = if (unit.nameRes != 0) stringResource(id = unit.nameRes) else ""
                        DropdownMenuItem(
                            text = { Text("${unit.symbol} ($uName)") },
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
                text = displayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            CopyUnitMenu(
                formats = formats,
                onCopy = { textToCopy ->
                    val message = toastFormat.format(textToCopy)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun CopyUnitMenu(
    formats: CopyFormats,
    onCopy: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.ContentCopy, contentDescription = stringResource(R.string.copy_options))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(formats.prefixUnit) },
                onClick = {
                    scope.launch {
                        val clipData = ClipData.newPlainText("prefix_unit", formats.prefixUnit)
                        clipboard.setClipEntry(ClipEntry(clipData))
                    }
                    onCopy(formats.prefixUnit)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(formats.powerUnit) },
                onClick = {
                    scope.launch {
                        val clipData = ClipData.newPlainText("power_unit", formats.powerUnit)
                        clipboard.setClipEntry(ClipEntry(clipData))
                    }
                    onCopy(formats.powerUnit)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(formats.fullNameUnit) },
                onClick = {
                    scope.launch {
                        val clipData = ClipData.newPlainText("fullname_unit", formats.fullNameUnit)
                        clipboard.setClipEntry(ClipEntry(clipData))
                    }
                    onCopy(formats.fullNameUnit)
                    expanded = false
                }
            )
        }
    }
}