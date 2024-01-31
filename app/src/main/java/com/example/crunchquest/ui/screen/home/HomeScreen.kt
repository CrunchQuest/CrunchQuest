package com.example.crunchquest.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crunchquest.R
import com.example.crunchquest.data.CardItem
import com.example.crunchquest.data.components.CardState
import com.example.crunchquest.data.components.CardValues
import com.example.crunchquest.data.components.CarouselLayout
import com.example.crunchquest.data.components.QuestCard
import com.example.crunchquest.data.components.QuestSheet
import com.example.crunchquest.data.components.StatusIndicator
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val cardValues = CardValues(0, 0, 0, "Novice", 0)
    val cardState = remember { mutableStateOf(CardState.Collapsed) }

    var openBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("CrunchQuest") },
                actions = {
                    // Notification Icon
                    IconButton(onClick = { /* Handle notification icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.Bell, tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }

                    // Profile Icon
                    IconButton(onClick = { /* Handle profile icon click */ }) {
                        FaIcon(
                            faIcon = FaIcons.UserCircle, tint = LocalContentColor
                                .current.copy(
                                    alpha =
                                    LocalContentAlpha.current
                                )
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
            )

//            TabLayout()
//            Ini Isinya terkait homescreen
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { androidx.compose.material3.Text(text = "Create") },
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Plus,
                        tint = androidx.compose.material3.LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    )
                },
                onClick = { openBottomSheet = true },
                containerColor = MaterialTheme.colors.surface
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
                    CarouselLayout()
                }


                item {
                    StatusIndicator(cardValues, cardState)
                }

                QuestList()



            }

        }
    )
    if (openBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { openBottomSheet = false }
        ) {
            //sheetContent
            QuestSheet()

        }
    }
}

fun LazyListScope.QuestList() {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quest",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            FaIcon(
                faIcon = FaIcons.LongArrowAltRight,
                tint = androidx.compose.material3.LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .align(Alignment.CenterVertically),
                size = 24.dp
            )

        }

    }
    items(10) {
        QuestCard(
            cardItem = CardItem(
                imageResource = R.drawable.food1,
                profile = "John Doe",
                title = "Benerin AC",
                rewards = "Rewards: 1000 CrunchCoins",
                onAccept = { /* Handle accept click */ }
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
//                    Spacer(modifier = Modifier.height(16.dp))
    
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}