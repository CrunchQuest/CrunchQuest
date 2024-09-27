package com.crunchquest.android.domain.repository

import com.crunchquest.android.domain.entities.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getCategory(categoryId: String): Result<Category>
    suspend fun createCategory(category: Category): Result<Category>
    suspend fun getAllCategories(): Result<List<Category>>
    suspend fun updateCategory(category: Category): Result<Unit>
    suspend fun deleteCategory(categoryId: String): Result<Unit>
}
