package com.crunchquest.android.data.model.remote

data class CategoryRemote(
    val categoryId: String = "",
    val categoryName: String = "",
    val description: String? = null,
    val status: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)