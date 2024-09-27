package com.crunchquest.android.data.repository

import com.crunchquest.android.data.mapper.CategoryMapper
import com.crunchquest.android.data.source.local.CategoryLocalDataSource
import com.crunchquest.android.data.source.remote.CategoryRemoteDataSource
import com.crunchquest.android.domain.entities.Category
import com.crunchquest.android.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val remoteDataSource: CategoryRemoteDataSource,
    private val localDataSource: CategoryLocalDataSource,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {

    override suspend fun createCategory(category: Category): Result<Category> {
        return try {
            val categoryRemote = categoryMapper.toRemote(category)
            remoteDataSource.createCategory(categoryRemote)
            localDataSource.saveCategory(categoryMapper.toLocal(category)) // Cache locally
            Result.success(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val localCategories = localDataSource.getCategories().map { categoryMapper.fromLocal(it) }
            if (localCategories.isNotEmpty()) {
                return Result.success(localCategories)
            }

            val remoteCategories = remoteDataSource.getCategories()?.map { categoryMapper.fromRemote(it) }
            if (remoteCategories != null) {
                remoteCategories.forEach { category ->
                    localDataSource.saveCategory(categoryMapper.toLocal(category)) // Cache locally
                }
                return Result.success(remoteCategories)
            }

            Result.failure(Exception("No categories found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategory(categoryId: String): Result<Category> {
        return try {
            val categoryLocal = localDataSource.getCategory(categoryId)
            if (categoryLocal != null) {
                return Result.success(categoryMapper.fromLocal(categoryLocal))
            }

            val categoryRemote = remoteDataSource.getCategory(categoryId)
            if (categoryRemote != null) {
                val category = categoryMapper.fromRemote(categoryRemote)
                localDataSource.saveCategory(categoryMapper.toLocal(category)) // Cache locally
                return Result.success(category)
            } else {
                Result.failure(Exception("Category not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllCategories(): Result<List<Category>> {
        return try {
            val localCategories = localDataSource.getAllCategories().map { categoryMapper.fromLocal(it) }
            if (localCategories.isNotEmpty()) {
                return Result.success(localCategories)
            }

            val remoteCategories = remoteDataSource.getAllCategories()?.map { categoryMapper.fromRemote(it) }
            if (remoteCategories != null) {
                remoteCategories.forEach { category ->
                    localDataSource.saveCategory(categoryMapper.toLocal(category)) // Cache locally
                }
                return Result.success(remoteCategories)
            }

            Result.failure(Exception("No categories found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        return try {
            val categoryRemote = categoryMapper.toRemote(category)
            remoteDataSource.updateCategory(categoryRemote)
            localDataSource.updateCategory(categoryMapper.toLocal(category))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            val categoryLocal = localDataSource.getCategory(categoryId)
            if (categoryLocal != null) {
                remoteDataSource.deleteCategory(categoryId)
                localDataSource.deleteCategory(categoryLocal)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Category not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
