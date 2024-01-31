package com.example.crunchquest.data

data class CardItem(
    val imageProfile: Int,
    val profile: String,
    val title: String,
    val rewards: String,
    val onAccept: () -> Unit
)