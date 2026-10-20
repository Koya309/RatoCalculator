package com.example.ratocalculator.settings.section

import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ratocalculator.R
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import com.example.ratocalculator.core.utils.LockScreenOrientation
import com.example.ratocalculator.feature.unitconverter.UnitConverterScreen
import com.example.ratocalculator.feature.unitconverter.UnitDisplayMode

@Composable
fun UnitConverterSection(
    sideBlock: Boolean,
    AdvancedPrefix: Boolean,
    unitDisplayMode: UnitDisplayMode,
    onNavigateToSideBlock: () -> Unit,
    onNavigateToAdvancedPrefix: () -> Unit,
    onNavigateToUnitDisplayMode: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.unit_converter),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSideBlock() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.side_block),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (sideBlock) stringResource(R.string.on) else stringResource(R.string.off),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToAdvancedPrefix() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.advanced_prefix),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (AdvancedPrefix) stringResource(R.string.on) else stringResource(R.string.off),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToUnitDisplayMode() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.unit_display_format),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    // 🟢 แก้ไขตรงนี้: ใช้ stringResource ดึงชื่อจาก titleRes
                    text = stringResource(unitDisplayMode.titleRes),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SideBlockScreen(
    sideBlock: Boolean,
    onSideBLock: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (sideBlock) stringResource(R.string.on) else stringResource(R.string.off),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = sideBlock,
                onCheckedChange = onSideBLock
            )
        }
    }
}

@Composable
fun AdvancedPrefixScreen(
    AdvancedPrefix: Boolean,
    onAdvancedPrefix: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (AdvancedPrefix) stringResource(R.string.on) else stringResource(R.string.off),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = AdvancedPrefix,
                onCheckedChange = onAdvancedPrefix
            )
        }
    }
}

@Composable
fun UnitDisplayModeScreen(
    currentMode: UnitDisplayMode,
    onUnitDisplayMode: (UnitDisplayMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(R.string.unit_display_format),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        UnitDisplayMode.entries.forEach { mode ->
            val isSelected = currentMode == mode

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = { onUnitDisplayMode(mode) },
                        role = Role.RadioButton
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = null
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = stringResource(mode.titleRes),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(mode.descriptionRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    RatoCalculatorTheme {
        UnitConverterScreen()
    }
}