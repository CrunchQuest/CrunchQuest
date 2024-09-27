package com.crunchquest.android.domain.entities

data class Category(
    val categoryId: String,
    val categoryName: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val status: String
)
