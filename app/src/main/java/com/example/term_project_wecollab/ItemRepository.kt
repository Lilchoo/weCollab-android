package com.example.term_project_wecollab

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ItemRepository @Inject constructor() {
    suspend fun getItem(itemId: String) : DocumentSnapshot {
        val db = Firebase.firestore
        return db.collection("items").document(itemId).get().await()
    }

    suspend fun getItemsByCategory(category: String) : QuerySnapshot {
        val db = Firebase.firestore
        return db.collection("items").whereEqualTo("category", category).get().await()
    }

    suspend fun getItemsByName(name: String) : QuerySnapshot {
        val db = Firebase.firestore
        return db.collection("items").whereEqualTo("name", name).get().await()

    }
}