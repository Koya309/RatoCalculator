package com.example.ratocalculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val subScreens = listOf(
        "trailing",
        "decimal",
        "about",
        "sideblock",
        "advancedprefix",
        "unitdisplaymode",
        "theme",
        "language",
        "reset_default"
    )
    val isSubScreen = currentRoute in subScreens

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(stringResource(R.string.menu), modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerContent(
                    navController = navController,
                    drawerState = drawerState,
                    scope = scope
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // NavHost Main
                        when (currentRoute) {
                            "basic" -> Text(stringResource(R.string.basic_calculator))
                            "scientific" -> Text(stringResource(R.string.scientific_calculator))
                            "settings" -> Text(stringResource(R.string.settings))
                            "unit_converter" -> Text(stringResource(R.string.unit_converter))
                            "physics" -> Text(stringResource(R.string.physics_calculator))

                            "trailing" -> Text(stringResource(R.string.remove_trailing_zero))
                            "decimal" -> Text(stringResource(R.string.decimal_places))
                            "about" -> Text(stringResource(R.string.about))
                            "sideblock" -> Text(stringResource(R.string.side_block))
                            "advancedprefix" -> Text(stringResource(R.string.advanced_prefix))
                            "unitdisplaymode" -> Text(stringResource(R.string.unit_display_format))
                            "theme" -> Text(stringResource(R.string.theme))
                            "language" -> Text(stringResource(R.string.language))
                            "reset_default" -> Text(stringResource(R.string.reset_setting))

                            else -> Text(stringResource(R.string.app_name))
                        }
                    },
                    navigationIcon = {
                        // Icon Nav for Back
                        if (isSubScreen) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        } else {
                            // Menu
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open()
                                        else drawerState.close()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = stringResource(R.string.menu)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // innerPadding in NavHost Hide TopAppBar
            ) {
                CalculatorNavHost(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}