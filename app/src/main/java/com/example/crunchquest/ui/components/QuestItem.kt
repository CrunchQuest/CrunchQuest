package com.example.crunchquest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.crunchquest.R
import com.example.crunchquest.theme.CrunchQuestTheme

@Composable
fun QuestItem(
    id: Long,
    username: String,
    title: String,
    reward: String,
    modifier: Modifier = Modifier,
    image: String,
    navigateToDetail: (Long) -> Unit

) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .border(1.dp, Color.LightGray.copy(0.5f), MaterialTheme.shapes.small)
            .clickable(onClick = { navigateToDetail(id) }),
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.h6)
                Text(text = "Reward:", style = MaterialTheme.typography.body2)
                Text(text = reward)
            }
            Column {
                Image(painterResource(id = R.drawable.food3),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                )
                Text(text = username,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(8.dp))
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    CrunchQuestTheme() {
        QuestItem(
            id = 0,
            username = "Satria",
            title = "Belikan Bensin Pertalite 2L",
            reward = "15.000",
            image = "",
            navigateToDetail = {},
        )
    }

}