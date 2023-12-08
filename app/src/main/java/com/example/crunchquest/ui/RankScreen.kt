package com.example.crunchquest.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
fun RankScreen() {
    androidx.compose.material3.Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rank") },
                actions = {
                    // Medal Icon
                    IconButton(onClick = { /* Handle medal icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Medal,
                            tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }

                    // Ticket Icon
                    IconButton(onClick = { /* Handle ticker icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.TicketAlt,
                            tint = LocalContentColor
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
                    RankContent()
                }

            }

        }
    )
}

@Composable
fun RankContent() {

}


@Preview
@Composable
fun PreviewRankScreen() {
    RankScreen()
}