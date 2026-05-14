package com.example.ratocalculator.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ratocalculator.core.theme.RatoCalculatorTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ratocalculator.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
    ){
    val decimalPlaces by viewModel.decimalPlaces.collectAsState(initial = 2)
    val removeTrailingZero by viewModel.removeTrailingZero.collectAsState(initial = true)
    // ใช้ LazyColumn เพื่อความลื่นไหลและประหยัดทรัพยากร (เหมาะกับเครื่องเก่า)
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // เว้นระยะห่างระหว่าง Section
    ) {
        item { SettingHeader() }

        item { CalculatorSection(
            decimalPlaces = decimalPlaces,
            removeTrailingZero = removeTrailingZero,
            onDecimalChange = { newValue -> viewModel.updateDecimalPlaces(newValue) },
            onToggleTrailing = { newValue -> viewModel.updateRemoveTrailingZero(newValue) }
        )}

        item { ThemeSection() }      // หมวด 2

        item { AboutSection() }               // หมวด 3

        item { ResetSection() }      // ส่วน Reset
    }
}

@Composable
fun CalculatorSection(
    decimalPlaces: Int,
    removeTrailingZero: Boolean,
    onDecimalChange: (Int) -> Unit,
    onToggleTrailing: (Boolean) -> Unit
    ){
    Column(
        modifier = Modifier.padding(all = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "ตัด .0 ถ้าเป็นจำนวนเต็ม")
            Switch(
                checked = removeTrailingZero,
                onCheckedChange = { onToggleTrailing(it) }
            )
        }
        Spacer(Modifier.height(16.dp))

        Text(text = "จำนวนทศนิยม")

        Slider(
            value = decimalPlaces.toFloat(),
            onValueChange = { onDecimalChange(it.toInt()) },
            valueRange = 0f..5f,
            steps = 5
        )

        Text(text = "แสดง ${decimalPlaces} ตำแหน่ง")
    }
}

@Composable
fun SettingHeader() {

    // โค้ดเลือกก้อนสี
}

@Composable
fun ThemeSection() {
    // โค้ดเลือกก้อนสี
}

@Composable
fun ResetSection() {
//    Button(
//        onClick = { viewModel.resetToDefault() },
//        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
//    ) {
        Text("คืนค่าเริ่มต้น")
//    }
}

@Composable
fun AboutSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // จัดกึ่งกลางเพื่อความสวยงาม
    ) {
        // 1. สัญลักษณ์แอป (App Icon)
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // เปลี่ยนเป็นโลโก้ RatoLab ของคุณ
            contentDescription = "App Logo",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2. ชื่อแอปและเวอร์ชัน
        Text(
            text = "RatoLab Calculator",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. ข้อมูลผู้พัฒนา และ GitHub
        Text(
            text = "Developed by YourName",
            style = MaterialTheme.typography.bodyMedium
        )

        TextButton(onClick = { /* เปิด Link GitHub */ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // เตรียม Icon GitHub ไว้ใน drawable
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(text = "GitHub / RatoLab")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RatoCalculatorTheme {
        SettingsScreen()
    }
}