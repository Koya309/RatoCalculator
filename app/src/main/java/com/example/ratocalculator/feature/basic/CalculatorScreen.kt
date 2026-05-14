package com.example.ratocalculator.feature.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.core.database.HistoryEntity
import com.example.ratocalculator.settings.SettingsViewModel
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by calculatorViewModel.uiState.collectAsState()
    val historyItems by calculatorViewModel.historyList.collectAsState()
    val decimalPlaces by settingsViewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by settingsViewModel.removeTrailingZero.collectAsState(initial = true)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f),
                drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                HistorySection(
                    historyItems = historyItems,
                    onItemClick = { item ->
                        calculatorViewModel.onHistoryItemClick(item)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        CalculatorContent(
            uiState = uiState,
            decimalPlaces = decimalPlaces,
            removeTrailingZero = removeTrailingZero,
            onButtonClick = { label ->
                calculatorViewModel.onButtonClick(
                    label,
                    decimalPlaces,
                    removeTrailingZero
                )
            },
            onHistoryClick = {
                scope.launch { drawerState.open() }
            }
        )
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun CalculatorContent(
    uiState: CalculatorState,
    decimalPlaces: Int,
    removeTrailingZero: Boolean,
    onButtonClick: (String) -> Unit,
    onHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101820))
            .padding(16.dp)
    ) {
        // Display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF1C1C1E), RoundedCornerShape(16.dp))
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onHistoryClick) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = "History",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = uiState.input.ifEmpty { " " },
                fontSize = 28.sp,
                color = Color.LightGray,
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = uiState.result,
                fontSize = 48.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid
        val buttons = listOf(
            listOf("C", "+/-", "( )", "DEL"),
            listOf("7", "8", "9", "÷"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+")
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { label ->
                        CalculatorButton(
                            label = label,
                            modifier = Modifier.weight(1f).aspectRatio(1f),
                            onClick = { onButtonClick(label) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (label in listOf("÷", "×", "-", "+", "=", "+/-"))
                colorResource(id = R.color.clear_button)
            else
                colorResource(id = R.color.operator_button),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (label == "DEL" || label == "backspace") {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Backspace,
                contentDescription = "Backspace",
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = label,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HistoryRow(item: HistoryEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(text = item.expression, color = Color.LightGray, fontSize = 16.sp)
        Text(text = item.result, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color.DarkGray)
    }
}

@Composable
fun HistorySection(
    historyItems: List<HistoryEntity>,
    onItemClick: (HistoryEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("History", fontSize = 24.sp, color = Color.White)
        LazyColumn {
            items(historyItems) { item ->
                HistoryRow(item = item, onClick = { onItemClick(item) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatoCalculatorAppPreview() {
    RatoCalculatorTheme {
        CalculatorContent(
            uiState = CalculatorState(input = "10+5", result = "15"),
            decimalPlaces = 2,
            removeTrailingZero = true,
            onButtonClick = {},
            onHistoryClick = {} // เพิ่มตรงนี้เพื่อให้ Preview ทำงานได้
        )
    }
}