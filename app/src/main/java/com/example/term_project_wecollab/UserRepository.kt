package com.example.term_project_wecollab

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(private val itemRepository: ItemRepository) {

    suspend fun getUser(uid: String) : DocumentSnapshot {
        val db = Firebase.firestore
        return db.collection("users").document(uid).get().await()
    }

    suspend fun createUser(uid: String, user: User) {
        val db = Firebase.firestore
        db.collection("users").document(uid).set(user).await()
    }

    suspend fun updateName(uid: String, name: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid).update("name", name).await()
    }

    suspend fun getUsers() : QuerySnapshot {
        val db = Firebase.firestore
        return db.collection("users").get().await()
    }

    suspend fun addToPurchases(uid: String, purchase: User.Purchase) {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)
        userRef.update("purchases", FieldValue.arrayUnion(purchase)).await()
    }

    suspend fun getOrderNumber(uid: String): Int? {
        val db = Firebase.firestore
        val userDoc = db.collection("users").document(uid).get().await()
        val user = userDoc.toObject(User::class.java)
        val length = user?.purchases?.size
        return if (length == 0) {
            0
        } else {
            user?.purchases?.lastIndex
        }
    }

    suspend fun getPurchaseByOrderNumber(uid: String, orderNumber: String) : User.Purchase? {
        val db = Firebase.firestore
        val userDoc = db.collection("users").document(uid).get().await()
        val user = userDoc.toObject(User::class.java) ?: return null
        for (purchase in user.purchases) {
            if (purchase.orderNumber == orderNumber) {
                return purchase
            }
        }
        return null
    }

    suspend fun addToCart(uid: String, item: User.Purchase.PurchaseItem) {
        val db = Firebase.firestore
        val userDoc = db.collection("users").document(uid).get().await()
        val user = userDoc.toObject(User::class.java) ?: return
        val cart = user.cart
        var itemFound = false
        for (cartItem in cart) {
            if (cartItem.itemId == item.itemId && cartItem.sizeSelected == item.sizeSelected) {
                cartItem.amount++
                itemFound = true
                break
            }
        }
        if (!itemFound) {
            cart.add(item)
        }
        db.collection("users").document(uid).update("cart", cart).await()
    }

    suspend fun getCart(uid: String) : ArrayList<Item>{
        val items = arrayListOf<Item>()
        val db = Firebase.firestore
        val userDoc = db.collection("users").document(uid).get().await()
        val user = userDoc.toObject(User::class.java) ?: return ArrayList()
        val cart = user.cart
        for (cartItem in cart) {
            val itemDoc = itemRepository.getItem(cartItem.itemId)
            val item = itemDoc.toObject(Item::class.java)
            if (item != null) {
                item.amount = cartItem.amount
                item.sizeSelected = cartItem.sizeSelected
                items.add(item)
            }
        }
        return items
    }

    suspend fun clearCart(uid: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid).update(
            "cart", arrayListOf<User.Purchase.PurchaseItem>()
        ).await()
    }
}