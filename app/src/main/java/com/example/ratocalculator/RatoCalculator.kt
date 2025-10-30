package com.example.ratocalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.ratocalculator.navigation.CalculatorNavHost
import com.example.ratocalculator.navigation.NavigationDrawerContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatoCalculator() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("เมนู", modifier = Modifier.padding(16.dp))
                NavigationDrawerContent(
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    ) {
        Scaffold(
            // 1. เพิ่ม Top Bar
            topBar = {
                TopAppBar(
                    title = { Text("เครื่องคำนวณ Rato") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {  } // เปิด Drawer เมื่อกด
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "เมนูหลัก"
                            )
                        }
                    }
                )
            },
            // 2. เพิ่ม Bottom Bar
        ) { innerPadding ->
            // ส่วนของเนื้อหาหลัก
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // สำคัญ: ใช้ innerPadding เพื่อไม่ให้เนื้อหาถูกบังโดย Top/Bottom Bar
            ) {
                 CalculatorNavHost(navController = navController, modifier = Modifier.fillMaxSize())
            }
        }
    }
}