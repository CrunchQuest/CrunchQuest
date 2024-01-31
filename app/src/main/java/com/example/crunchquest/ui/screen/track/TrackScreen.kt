package com.example.crunchquest.ui.screen.track

import android.annotation.SuppressLint
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.crunchquest.ui.components.TabLayout
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Track")},
                actions = {
                    IconButton(onClick = { /* Handle notification icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Bell, tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )

            TabLayout()
        },


        content = {

        }
    )

}

@Composable
fun PendingScreen() {
    Text("Pending Screen")
}

@Composable
fun OngoingScreen() {
    Text("Ongoing Screen")
}

@Composable
fun CompleteScreen() {
    Text("Complete Screen")
}

@Composable
fun CanceledScreen() {
    Text("Canceled Screen")
}

@Preview
@Composable
fun PreviewTrackScreen() {
    TrackScreen()
}
