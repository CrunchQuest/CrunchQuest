package com.crunchquest.android.data.remote.model

data class NotificationResponse(
    val notificationId: String,
    val title: String,
    val message: String,
    val createdAt: String
)