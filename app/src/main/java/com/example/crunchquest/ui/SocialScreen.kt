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
fun SocialScreen() {
    androidx.compose.material3.Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Social") },
                actions = {
                    // Video Icon
                    IconButton(onClick = { /* Handle like icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.PlusSquareRegular,
                            tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }

                    // Like Icon
                    IconButton(onClick = { /* Handle like icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Heart,
                            tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }

                    // Message Icon
                    IconButton(onClick = { /* Handle message icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.FacebookMessenger,
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
                    SocialContent()
                }

            }

        }
    )
}

@Composable
fun SocialContent() {

}


@Preview
@Composable
fun PreviewSocialScreen() {
    SocialScreen()
}