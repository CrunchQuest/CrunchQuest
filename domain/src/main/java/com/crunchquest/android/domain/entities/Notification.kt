package com.crunchquest.android.domain.entities

data class Notification(
    val notificationId: String,
    val userId: String,
    val type: String,
    val title: String,
    val message: String,
    val createdAt: String,
    var status: String
)
