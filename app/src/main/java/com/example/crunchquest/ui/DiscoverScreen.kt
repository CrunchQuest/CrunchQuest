package com.example.crunchquest.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen() {
    androidx.compose.material3.Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material.Text("Discover") },
                actions = {
                    // Live Icon
                    IconButton(onClick = { /* Handle live icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Video,
                            tint = androidx.compose.material.LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }

                    // Search Icon
                    IconButton(onClick = { /* Handle search icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Search,
                            tint = androidx.compose.material.LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                item {
                    DiscoverContent()
                }

            }

        }
    )
}

@Composable
fun DiscoverContent() {
    
}


@Preview
@Composable
fun PreviewDiscoverScreen() {
    DiscoverScreen()
}