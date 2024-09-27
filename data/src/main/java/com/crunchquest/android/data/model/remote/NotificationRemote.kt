package com.crunchquest.android.data.model.remote

data class NotificationRemote(
    val notificationId: String = "",
    val userId: String = "",
    val type: String = "",
    val title: String = "",
    val message: String = "",
    val createdAt: String = "",
    val status: String = ""
)