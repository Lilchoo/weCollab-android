package com.example.term_project_wecollab

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    fun getUserInfo(uid: String) : Task<DocumentSnapshot> {
        val docRef = Firebase.firestore.collection("users").document(uid)
        return docRef.get()
    }

    fun updateName(uid: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.updateName(uid, name)
        }
    }

    suspend fun addPurchase(uid: String, itemList: ArrayList<Item>) {

        val purchaseDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val purchaseDate = purchaseDateTime.format(formatter)

        val purchase = User.Purchase(
            orderNumber = OrderNumberGenerator.generate(),
            purchaseDate = purchaseDate,
            items = arrayListOf(),
            totalPrice = 0f,
            totalItems = 0
        )

        var totalSum = 0.0

        for (item in itemList) {
            val purchaseItem = User.Purchase.PurchaseItem(
                itemId = item.itemId,
                amount = item.amount,
                sizeSelected = item.sizeSelected
            )

            totalSum += (item.price * item.amount)
            purchase.items.add(purchaseItem)
        }

        purchase.totalItems = itemList.size
        purchase.totalPrice = totalSum.toFloat()
        userRepository.addToPurchases(uid, purchase)
    }

    suspend fun getPurchase(uid: String, orderNumber: String) : User.Purchase? {
        val result = CoroutineScope(Dispatchers.IO).async {
            return@async userRepository.getPurchaseByOrderNumber(uid, orderNumber)
        }.await()
        return result
    }
    fun addToCart(uid: String, item: User.Purchase.PurchaseItem) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.addToCart(uid, item)
        }
    }

    suspend fun getCart(uid: String) : ArrayList<Item> {
        val items = CoroutineScope(Dispatchers.IO).async {
            return@async userRepository.getCart(uid)
        }.await()
        return items
    }

    fun removeCart(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.clearCart(uid)
        }
    }
}