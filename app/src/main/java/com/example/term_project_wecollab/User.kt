package com.example.term_project_wecollab

data class User(
    val name: String = "",
    val purchases: ArrayList<Purchase> = arrayListOf(),
    val cart: ArrayList<Purchase.PurchaseItem> = arrayListOf()
) {
    data class Purchase(
        val orderNumber: String = "00000",
        val items: ArrayList<PurchaseItem> = arrayListOf(),
        val purchaseDate: String = "",
        var totalPrice: Float = 0f,
        var totalItems: Int = 0
    ) {
        data class PurchaseItem(
            val itemId: String = "",
            var amount: Int = 0,
            val sizeSelected: String = ""
        )
    }
}