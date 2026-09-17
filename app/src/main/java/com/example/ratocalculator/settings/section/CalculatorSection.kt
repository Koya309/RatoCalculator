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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ratocalculator.R
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import com.example.ratocalculator.core.utils.LockScreenOrientation

@Composable
fun BasicCalculatorSection(
    removeTrailingZero: Boolean,
    decimalPlaces: Int,
    onNavigateToTrailing: () -> Unit,
    onNavigateToDecimal: () -> Unit,
){
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ){
        // Column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            // Main headline
            Text(
                text = stringResource(R.string.basic_calculator),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onNavigateToTrailing() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
            Column{
                Text(
                    text = stringResource(R.string.remove_trailing_zero),
                    style = MaterialTheme.typography.bodyLarge
                    )
                }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (removeTrailingZero) stringResource(R.string.on) else stringResource(R.string.off),
                    color = MaterialTheme.colorScheme.primary)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ onNavigateToDecimal() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column {
                    Text(
                        text = stringResource(R.string.decimal_places),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "$decimalPlaces", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
@Composable
fun TrailingZeroDetailScreen(
    removeTrailingZero: Boolean,
    onToggleTrailing: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // On/off switch control unit
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (removeTrailingZero) stringResource(R.string.on) else stringResource(R.string.off),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Switch(checked = removeTrailingZero, onCheckedChange = onToggleTrailing)
        }

        // The sample data table structure (ex | off | or | on)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //Text("ex", fontWeight = FontWeight.Bold)
                    Text(stringResource(R.string.off)
                        , color = Color.Gray
                    )
                    Text(""
                        , color = Color.Gray)

                    Text(stringResource(R.string.on),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                TextRow("1.00", "1")
                TextRow("2.00", "2")
                TextRow("3.04", "3") // อ้างอิงตามเลขที่แก้ในกระดาษแผ่นแรก
            }
        }
    }
}

@Composable
fun DecimalPlacesScreen(
    decimalPlaces: Int,
    onDecimalChange: (Int) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
        // The decimal point adjustment scale is consistent and uses the same logic system.
        Column {
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


@Composable
fun TextRow(offStr: String, onStr: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(offStr, color = Color.Gray)
        Text("-")
        Text(onStr, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun RatoCalculatorAppPreview() {
    RatoCalculatorTheme {
        BasicCalculatorSection(
            removeTrailingZero = true,
            decimalPlaces = 2,
            onNavigateToTrailing = {},
            onNavigateToDecimal = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrailingZeroDetailScreenPreview() {
    RatoCalculatorTheme {
        TrailingZeroDetailScreen(
            removeTrailingZero = true,
            onToggleTrailing = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DecimalPlacesScreenPreview() {
    RatoCalculatorTheme {
        DecimalPlacesScreen(
            decimalPlaces = 3,
            onDecimalChange = {},
        )
    }
}