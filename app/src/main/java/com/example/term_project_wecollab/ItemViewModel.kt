package com.example.term_project_wecollab

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(private val itemRepository: ItemRepository): ViewModel() {

    suspend fun getItem(itemId: String) : Item? {
        val item = CoroutineScope(Dispatchers.IO).async {
            itemRepository.getItem(itemId)
        }.await()
        return item.toObject(Item::class.java)
    }

    suspend fun getItemsByName(name: String) : List<Item> {
        val items = CoroutineScope(Dispatchers.IO).async {
            itemRepository.getItemsByName(name)
        }.await()
        val itemList = mutableListOf<Item>()
        for (item in items) {
            itemList.add(item.toObject(Item::class.java))
        }
        return itemList
    }

    suspend fun getItemsByCategory(category: String) : List<Item> {
        val items = CoroutineScope(Dispatchers.IO).async {
            itemRepository.getItemsByCategory(category)
        }.await()
        val itemList = mutableListOf<Item>()
        for (item in items) {
            itemList.add(item.toObject(Item::class.java))
        }
        return itemList
    }


}