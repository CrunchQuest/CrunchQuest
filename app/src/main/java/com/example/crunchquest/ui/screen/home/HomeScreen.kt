package com.example.crunchquest.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crunchquest.data.model.QuestList
import com.example.crunchquest.di.Injection
import com.example.crunchquest.ui.ViewModelFactory
import com.example.crunchquest.ui.common.UiState
import com.example.crunchquest.ui.components.QuestItem
import com.example.crunchquest.ui.components.QuestSheet
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepository())),
    navigateToDetail: (Long) -> Unit
) {
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

                },
                backgroundColor = MaterialTheme.colors.background,
            )

//            TabLayout()
//            Ini Isinya terkait homescreen
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Create") },
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
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 32.dp),
//                contentPadding = PaddingValues(16.dp),
//            ) {
//                item {
//                    CarouselLayout()
//                }
//            }

//                item {
//                    StatusIndicator(cardValues, cardState)
//                }
//
//                QuestItem()
//
//            }
            viewModel.uiState.collectAsState(initial = UiState.Loading).value.let {
                when (it) {
                    is UiState.Loading -> {
                        viewModel.getAllData()
                    }
                    is UiState.Success -> {
                        HomeContent(
                            questList = it.data,
                            navigateToDetail = navigateToDetail,
                        )
                    }
                    is UiState.Error -> {}
                }
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

//fun LazyListScope.QuestItem() {
//    item {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 12.dp, end = 12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Quest",
//                style = MaterialTheme.typography.h6,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//            FaIcon(
//                faIcon = FaIcons.LongArrowAltRight,
//                tint = androidx.compose.material3.LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
//                modifier = Modifier
//                    .padding(bottom = 4.dp)
//                    .align(Alignment.CenterVertically),
//                size = 24.dp
//            )
//
//        }
//
//    }
//    items(10) {
//        QuestCard(
//            cardItem = CardItem(
//                imageProfile = R.drawable.food1,
//                profile = "John Doe",
//                title = "Benerin AC",
//                rewards = "Rewards: 1000 CrunchCoins",
//                onAccept = { /* Handle accept click */ }
//            )
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//    }
////                    Spacer(modifier = Modifier.height(16.dp))
//
//}


@Composable
fun HomeContent(
    questList: List<QuestList>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
) {
    Box(modifier = Modifier
        .padding(top = 16.dp)) {
        LazyColumn(modifier = Modifier
            .padding(8.dp)){
            items(questList) {
                QuestItem(
                    id = it.quest.id,
                    username = it.quest.userName,
                    title = it.quest.questTitle,
                    reward = it.quest.questReward,
                    image = it.quest.profileImage,
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        navigateToDetail = { }
    )
}