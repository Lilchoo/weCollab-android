package com.example.term_project_wecollab

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.term_project_wecollab.databinding.ItemPurchaseBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PurchaseHistoryItemDetailAdapter(
    private val purchaseItems: List<User.Purchase.PurchaseItem>,
    private val itemViewModel: ItemViewModel) :
    RecyclerView.Adapter<PurchaseHistoryItemDetailAdapter.ViewHolder>() {

    // Holds the views
    class ViewHolder(binding: ItemPurchaseBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemImage: ImageView = binding.imageViewPurchaseItemImage
        val itemName: TextView = binding.textViewPurchaseItemItemName
        val itemAmount: TextView = binding.textViewPurchaseItemAmount
        val itemPrice: TextView = binding.textViewPurchaseItemPrice
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ItemPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        CoroutineScope(Dispatchers.Main).launch {
            val itemDetails = itemViewModel.getItem(purchaseItems[position].itemId)
            if (itemDetails != null) {
                val uri = FirebaseStorage.getInstance().getReference(itemDetails.image).downloadUrl.await()
                val imageUrl = uri.toString()
                Glide.with(holder.itemImage.context).load(imageUrl).into(holder.itemImage)
                holder.itemName.text = itemDetails.name
                holder.itemAmount.text = String.format("x%d", purchaseItems[position].amount)
                val price = purchaseItems[position].amount * itemDetails.price
                holder.itemPrice.text = String.format("$%.2f", price)
            }

        }
        //holder.textView.text = mList[position]
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return purchaseItems.size
    }

}