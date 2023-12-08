package com.example.crunchquest.data.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class CardValues(val cc: Int, val ongoing: Int, val completed: Int, val profession: String, val badge: Int)

enum class CardState {
    Collapsed,
    Expanded
}

@Composable
fun StatusIndicator(cardValues: CardValues, cardState: MutableState<CardState>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                ) {
                    Text("CrunchCoins")
                    Text("${cardValues.cc} cc", textAlign = androidx.compose.ui.text.style.TextAlign.Center)

                }
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                ) {
                    Text("Ongoing")
                    Text("${cardValues.ongoing}", textAlign = androidx.compose.ui.text.style.TextAlign.Center)

                }
                Column(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                ) {
                    Text("Completed")
                    Text("${cardValues.completed}", textAlign = androidx.compose.ui.text.style.TextAlign.Center)

                }
                Column {
                    Icon(
                        imageVector = if (cardState.value == CardState.Collapsed) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.clickable { cardState.value = if (cardState.value == CardState.Collapsed) CardState.Expanded else CardState.Collapsed }
                    )
                }
        }


            // Check if the card is expanded
            if (cardState.value == CardState.Expanded) {
                // Third Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text("Profession")
                        Text(cardValues.profession, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                    Column {
                        Text("Badge")
                        Text("${cardValues.badge}", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }

                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CardLayoutPreview() {
        // You can customize the cardValues and cardState for previewing
        StatusIndicator(cardValues = CardValues(10, 5, 3, "Expert", 2), cardState = remember { mutableStateOf(CardState.Expanded) })
}