package com.example.ratocalculator.settings.Section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailingZeroDetailScreen(
    removeTrailingZero: Boolean,
    decimalPlaces: Int,
    onToggleTrailing: (Boolean) -> Unit,
    onDecimalChange: (Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Remove trailing zero") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // On/off switch control unit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (removeTrailingZero) "on" else "off", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Switch(checked = removeTrailingZero, onCheckedChange = onToggleTrailing)
            }

            // The sample data table structure (ex | off | or on)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("ex", fontWeight = FontWeight.Bold)
                        Text("off", color = Color.Gray)
                        Text("or on", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    TextRow("1.00", "1")
                    TextRow("2.00", "2")
                    TextRow("3.04", "3") // อ้างอิงตามเลขที่แก้ในกระดาษแผ่นแรก
                }
            }

            // The decimal point adjustment scale is consistent and uses the same logic system.
            Column {
                Text(text = "Decimal Places", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Slider(
                    value = decimalPlaces.toFloat(),
                    onValueChange = { onDecimalChange(it.toInt()) },
                    valueRange = 0f..5f,
                    steps = 4
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    (0..5).forEach { Text(it.toString(), color = if(decimalPlaces == it) MaterialTheme.colorScheme.primary else Color.Gray) }
                }
            }
        }
    }
}

@Composable
fun TextRow(offStr: String, onStr: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("-")
        Text(offStr, color = Color.Gray)
        Text(onStr, fontWeight = FontWeight.Bold)
    }
}