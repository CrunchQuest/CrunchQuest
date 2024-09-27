package com.crunchquest.android.data.source.remote

import com.crunchquest.android.data.model.remote.CategoryRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CategoryRemoteDataSource(private val firestore: FirebaseFirestore) {

    suspend fun createCategory(categoryRemote: CategoryRemote) {
        firestore.collection("categories").document(categoryRemote.categoryId).set(categoryRemote).await()
    }

    suspend fun getCategory(categoryId: String): CategoryRemote? {
        val categoryDocument = firestore.collection("categories").document(categoryId).get().await()
        return if (categoryDocument.exists()) categoryDocument.toObject(CategoryRemote::class.java) else null
    }

    suspend fun getCategories(): List<CategoryRemote>? {
        val result = firestore.collection("categories").get().await()
        return result.toObjects(CategoryRemote::class.java)
    }

    suspend fun getAllCategories(): List<CategoryRemote>? {
        val result = firestore.collection("categories").get().await()
        return result.toObjects(CategoryRemote::class.java)
    }

    suspend fun updateCategory(categoryRemote: CategoryRemote) {
        firestore.collection("categories").document(categoryRemote.categoryId).set(categoryRemote).await()
    }

    suspend fun deleteCategory(categoryId: String) {
        firestore.collection("categories").document(categoryId).delete().await()
    }
}
