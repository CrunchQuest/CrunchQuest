package com.crunchquest.android.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(tableName = "categories")
data class CategoryLocal(
    @PrimaryKey val categoryId: String,
    val categoryName: String,
    val description: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryLocal)

    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    suspend fun getCategory(categoryId: String): CategoryLocal?

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryLocal>

    @Update
    suspend fun updateCategory(category: CategoryLocal)

    @Delete
    suspend fun deleteCategory(category: CategoryLocal)
}

class CategoryLocalDataSource(private val categoryDao: CategoryDao) {

    suspend fun saveCategory(categoryLocal: CategoryLocal) {
        categoryDao.insertCategory(categoryLocal)
    }

    suspend fun getCategory(categoryId: String): CategoryLocal? {
        return categoryDao.getCategory(categoryId)
    }
    
    suspend fun getCategories(): List<CategoryLocal> {
        return categoryDao.getAllCategories()
    }

    suspend fun getAllCategories(): List<CategoryLocal> {
        return categoryDao.getAllCategories()
    }

    suspend fun updateCategory(categoryLocal: CategoryLocal) {
        categoryDao.updateCategory(categoryLocal)
    }

    suspend fun deleteCategory(categoryLocal: CategoryLocal) {
        categoryDao.deleteCategory(categoryLocal)
    }
}
