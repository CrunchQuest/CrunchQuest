package com.example.crunchquest.data.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

//@Composable
//fun TabLayout() {
//    val tabsName = remember { Tabs.values().map { it.value } }
//    val selectedIndex = remember { mutableStateOf(Tabs.NUTRITION.ordinal) }
//
//    Column {
//        TabRow(
//            selectedTabIndex = selectedIndex.value,
//            containerColor = MaterialTheme.colorScheme.surface
//        ) {
//            tabsName.forEachIndexed { index, title ->
//                Tab(
//                    selected = index == selectedIndex.value,
//                    onClick = {
//                        when (title) {
//                            Tabs.NUTRITION.value -> {
//                                selectedIndex.value = Tabs.NUTRITION.ordinal
//                            }
//                            Tabs.DEVELOPMENT.value -> {
//                                selectedIndex.value = Tabs.DEVELOPMENT.ordinal
//                            }
//                            Tabs.ALLIANCE.value -> {
//                                selectedIndex.value = Tabs.ALLIANCE.ordinal
//                            }
//                        }
//                    },
//                    text = { Text(title, color = MaterialTheme.colorScheme.onPrimaryContainer ) }
//                )
//            }
//        }
//        Surface(modifier = Modifier.weight(0.5f)) {
//            when (selectedIndex.value) {
//                Tabs.NUTRITION.ordinal -> {
//                    NutritionScreen()
//                }
//                Tabs.DEVELOPMENT.ordinal -> {
//                    DevelopmentScreen()
//                }
//                Tabs.ALLIANCE.ordinal -> {
//                    AllianceScreen()
//                }
//            }
//        }
//
//    }
//
//}