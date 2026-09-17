package com.example.ratocalculator.feature.calculator

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.ratocalculator.R
import com.example.ratocalculator.core.database.HistoryEntity
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import com.example.ratocalculator.core.utils.LockScreenOrientation
import com.example.ratocalculator.settings.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun CalculatorScreen(
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

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




@Composable
fun CalculatorContent(
    uiState: CalculatorState,
    decimalPlaces: Int,
    removeTrailingZero: Boolean,
    onButtonClick: (String) -> Unit,
    onHistoryClick: () -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val screenWidth = with(density) { windowInfo.containerSize.width.toDp().value }
    val screenHeight = with(density) { windowInfo.containerSize.height.toDp().value }

    val inputFontSize = (screenWidth * 0.080f).sp
    val resultFontSize = (screenWidth * 0.14f).sp

    val verticalPadding = if (screenHeight < 650) 12.dp else 20.dp

    // Column for Layout all on display
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        //  [TOP ROW]  History + Report FIX  TZ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHistoryClick) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = stringResource(R.string.history),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Box for report decimalPlaces
                Box(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFDEFF9A), RoundedCornerShape(6.dp))
                        .background(Color(0xFF1A241F))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FIX: $decimalPlaces",
                        color = Color(0xFFDEFF9A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Box for report removeTrailingZero
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (removeTrailingZero) Color(0xFFDEFF9A) else Color.Gray,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(if (removeTrailingZero) Color(0xFF1A241F) else Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (removeTrailingZero) "ON" else "OFF",
                        color = if (removeTrailingZero) Color(0xFFDEFF9A) else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2️ [DISPLAY BLOCK]
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Gray , RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = verticalPadding),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = uiState.input.ifEmpty { " " },
                fontSize = inputFontSize,
                color = Color.Black,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = uiState.result.ifEmpty { " " },
                fontSize = resultFontSize,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            )
        }

        Spacer(modifier = Modifier.height(if (screenHeight < 650) 8.dp else 16.dp))

        // 3️ [KEYBOARD GRID]
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
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
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
            onHistoryClick = {}
        )
    }
}