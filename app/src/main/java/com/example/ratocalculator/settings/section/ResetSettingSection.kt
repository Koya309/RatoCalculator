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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.core.utils.LockScreenOrientation
import com.example.ratocalculator.settings.ResetOption
import com.example.ratocalculator.settings.SettingsViewModel

@Composable
fun ResetSettingSection(
    onNavigateResetToDefault: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onNavigateResetToDefault() },
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
                text = stringResource(R.string.reset_setting),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.reset_setting),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ResetToDefaultScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var selectedResetOption by remember { mutableStateOf<ResetOption?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Basic Calculator
        ResetCardItem(
            title = stringResource(R.string.basic_calculator),
            description = "Reset calculator settings to default",
            onResetClick = { selectedResetOption = ResetOption.BASIC_CALCULATOR }
        )

        // 2. Unit Converter
        ResetCardItem(
            title = stringResource(R.string.unit_converter),
            description = "Reset unit converter settings to default",
            onResetClick = { selectedResetOption = ResetOption.UNIT_CONVERTER }
        )

        // 3. System / Theme & Language
        ResetCardItem(
            title = "System",
            description = "Reset theme and language settings to default",
            onResetClick = { selectedResetOption = ResetOption.THEME_AND_LANGUAGE }
        )

        // 4. Reset All Settings
        ResetCardItem(
            title = stringResource(R.string.reset_setting),
            description = "Reset all app settings to default",
            onResetClick = { selectedResetOption = ResetOption.ALL_SETTINGS }
        )
    }

    // Dialog Reset
    selectedResetOption?.let { option ->
        val dialogTitle = when (option) {
            ResetOption.BASIC_CALCULATOR -> stringResource(R.string.basic_calculator)
            ResetOption.UNIT_CONVERTER -> stringResource(R.string.unit_converter)
            ResetOption.THEME_AND_LANGUAGE -> "System"
            ResetOption.ALL_SETTINGS -> stringResource(R.string.reset_setting)
        }

        ResetConfirmDialog(
            title = dialogTitle,
            onConfirm = {
                viewModel.resetByOption(option)
                selectedResetOption = null
            },
            onDismiss = { selectedResetOption = null }
        )
    }
}

@Composable
private fun ResetCardItem(
    title: String,
    description: String,
    onResetClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = onResetClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Reset",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ResetConfirmDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = "Do you want to reset this setting to default?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Reset",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}