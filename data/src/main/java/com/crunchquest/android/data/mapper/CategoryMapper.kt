package com.crunchquest.android.data.mapper

import com.crunchquest.android.data.model.remote.CategoryRemote
import com.crunchquest.android.data.source.local.CategoryLocal
import com.crunchquest.android.domain.entities.Category
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    // Convert remote data model to domain entity
    fun fromRemote(categoryRemote: CategoryRemote): Category {
        return Category(
            categoryId = categoryRemote.categoryId,
            categoryName = categoryRemote.categoryName,
            description = categoryRemote.description,
            status = categoryRemote.status,
            createdAt = categoryRemote.createdAt,
            updatedAt = categoryRemote.updatedAt
        )
    }

    // Convert domain entity to remote data model
    fun toRemote(category: Category): CategoryRemote {
        return CategoryRemote(
            categoryId = category.categoryId,
            categoryName = category.categoryName,
            description = category.description,
            status = category.status,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )
    }

    // Convert local data model to domain entity
    fun fromLocal(categoryLocal: CategoryLocal): Category {
        return Category(
            categoryId = categoryLocal.categoryId,
            categoryName = categoryLocal.categoryName,
            description = categoryLocal.description,
            status = categoryLocal.status,
            createdAt = categoryLocal.createdAt,
            updatedAt = categoryLocal.updatedAt
        )
    }

    // Convert domain entity to local data model
    fun toLocal(category: Category): CategoryLocal {
        return CategoryLocal(
            categoryId = category.categoryId,
            categoryName = category.categoryName,
            description = category.description,
            status = category.status,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt
        )
    }
}
