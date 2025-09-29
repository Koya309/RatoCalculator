package com.example.ratocalculator.feature.unitconverter

import android.R.id.input
import android.R.id.tabs
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.ui.theme.RatoCalculatorTheme

// Test Github i try move Https to ssh

@Composable
fun UnitConverterScreen(
    viewModel: UnitConverterViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val tabs = UnitCategory.values()
    var selectedTab by remember { mutableStateOf(uiState.category.ordinal) }
    var kgInput by remember { mutableStateOf("7") }
    var input by remember { mutableStateOf("7") }
    var unit by remember { mutableStateOf("kg") }
    val kgValue = (kgInput.toDoubleOrNull() ?: 0.0)
    val gramValue = kgValue * 1000
    var expanded by remember { mutableStateOf(false) }



    Column(modifier = Modifier
        .fillMaxSize()
       // .padding(16.dp)
    ){
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

            Text(text = "Input")
            Row(
                verticalAlignment = Alignment.CenterVertically,
               // modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            ){
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Value") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))

                Box {
                    Button(onClick = { expanded = true }) {
                        Text(unit)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("kg") },
                            onClick = {
                                unit = "kg"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("g") },
                            onClick = {
                                unit = "g"
                                expanded = false
                            }
                        )
                    }
                }
            }
            }

            Text(text = "${kgInput.toDoubleOrNull() ?: 0.0} kilogram")

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text(text = "Gram")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            ) {
                TextField(
                    value = gramValue.toInt().toString(),
                    onValueChange = { /* read-only (optional) */ },
                    label = { Text("g") },
                    modifier = Modifier.weight(1f),
                    enabled = false // ไม่ให้แก้เอง
                )
            }
            Text(text = "${(kgInput.toDoubleOrNull() ?: 0.0) * 1000} gram")
            }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        UnitConverterScreen()
    }
}