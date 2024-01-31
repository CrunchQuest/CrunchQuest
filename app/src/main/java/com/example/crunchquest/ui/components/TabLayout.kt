package com.example.crunchquest.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crunchquest.ui.screen.track.CanceledScreen
import com.example.crunchquest.ui.screen.track.CompleteScreen
import com.example.crunchquest.ui.screen.track.OngoingScreen
import com.example.crunchquest.ui.screen.track.PendingScreen

@Composable
fun TabLayout() {
    val tabsName = remember { Tabs.values().map { it.value } }
    val selectedIndex = remember { mutableStateOf(Tabs.PENDING.ordinal) }

    Column(
        modifier = Modifier
            .padding(top = 47.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedIndex.value,
            backgroundColor = MaterialTheme.colors.background,
        ) {
            tabsName.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedIndex.value,
                    onClick = {
                        when (title) {
                            Tabs.PENDING.value -> {
                                selectedIndex.value = Tabs.PENDING.ordinal
                            }
                            Tabs.ONGOING.value -> {
                                selectedIndex.value = Tabs.ONGOING.ordinal
                            }
                            Tabs.COMPLETE.value -> {
                                selectedIndex.value = Tabs.COMPLETE.ordinal
                            }
                            Tabs.CANCELED.value -> {
                                selectedIndex.value = Tabs.CANCELED.ordinal
                            }
                        }
                    },
                    text = { Text(title, color = MaterialTheme.colors.onBackground ) }
                )
            }
        }
        Surface(modifier = Modifier
            .weight(0.5f)) {
            when (selectedIndex.value) {
                Tabs.PENDING.ordinal -> {
                    PendingScreen()
                }
                Tabs.ONGOING.ordinal -> {
                    OngoingScreen()
                }
                Tabs.COMPLETE.ordinal -> {
                    CompleteScreen()
                }
                Tabs.CANCELED.ordinal -> {
                    CanceledScreen()
                }
            }
        }

    }

}