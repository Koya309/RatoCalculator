package com.example.ratocalculator.settings.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.ratocalculator.R

@Composable
fun ResetSettingSection(
    onNavigateResetToDefault: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Main headline
            Text(
                text = stringResource(R.string.reset_setting),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateResetToDefault() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.reset_setting),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ResetToDefaultScreen(
    onResetClick: () -> Unit,
){
    var showDialog by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(
                    alpha = 0.2f
                )
            )
        ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
             Column {
                  Text(
                        stringResource(R.string.reset_setting),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                  )
                  Text(
                        stringResource(R.string.reset_setting),
                        style = MaterialTheme.typography.bodySmall
                  )}
             Button(
                  onClick = { showDialog = true },
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                  ){
                  Text("Reset")
                  }
             }
        }

            //I haven't finished it yet. Sorry~
    if (showDialog) {
         AlertDialog(
              onDismissRequest = { showDialog = false },
              title = { Text("Reset setting") },
              text = { Text("Do you want to clear all settings?") },
              confirmButton = {
                    TextButton(onClick = { onResetClick(); showDialog = false }) {
                        Text(
                            "Reset",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
              },
              dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("No") }
              }
         )
    }
}