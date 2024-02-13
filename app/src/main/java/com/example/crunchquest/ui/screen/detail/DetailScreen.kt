package com.example.crunchquest.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.crunchquest.di.Injection
import com.example.crunchquest.theme.CrunchQuestTheme
import com.example.crunchquest.ui.ViewModelFactory
import com.example.crunchquest.ui.common.UiState

@Composable
fun DetailScreen(
    id: Long,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(Injection.provideRepository())),
    navigateUp: () -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getById(id)
            }

            is UiState.Success -> {
                val data = uiState.data
                DetailItem(
                    quest_title = data.quest.questTitle,
                    quest_reward = data.quest.questReward,
                    profile_image = data.quest.profileImage,
                    onBackClick = navigateUp,
                )
            }

            is UiState.Error -> {}
        }
    }
    
}

@Composable
fun DetailItem(
    quest_title: String,
    quest_reward: String,
    profile_image: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = profile_image,
                    contentDescription = "Movie Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = quest_title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Reward",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = quest_reward,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailItemPreview() {
    CrunchQuestTheme{
        DetailItem(
            quest_title = "Movie Title",
            quest_reward = "200.000",
            profile_image = "movie image",
            onBackClick = {},
        )

    }

}