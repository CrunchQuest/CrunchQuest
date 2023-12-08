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
fun ShopScreen() {
    androidx.compose.material3.Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shop") },
                actions = {
                    // Cart Icon
                    IconButton(onClick = { /* Handle cart icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.ShoppingCart,
                            tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }

                    // bars Icon
                    IconButton(onClick = { /* Handle bars icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Bars,
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
                    ShopContent()
                }

            }

        }
    )
}

@Composable
fun ShopContent() {

}


@Preview
@Composable
fun PreviewShopScreen() {
    ShopScreen()
}