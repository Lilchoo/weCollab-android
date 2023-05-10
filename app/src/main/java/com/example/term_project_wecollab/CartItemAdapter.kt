package com.example.term_project_wecollab

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.term_project_wecollab.databinding.CartItemBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class CartItemAdapter(private val mList: List<Item>) : RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

    // Holds the views
    class ViewHolder(binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.imageViewCartProductImage
        val textName: TextView = binding.textViewCartProductName
        val textPrice: TextView = binding.textViewCartProductPrice
        val textSize: TextView = binding.textViewCartProductSize
        val textAmount: TextView = binding.textViewCartProductAmount
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageStorage = FirebaseStorage.getInstance()
        imageStorage.getReference(mList[position].image).downloadUrl.addOnSuccessListener {
            val imageUri = it.toString()
            Glide.with(holder.imageView.context).load(imageUri).into(holder.imageView)
        }
        holder.textName.text = mList[position].name
        holder.textPrice.text = mList[position].price.toString()
        holder.textSize.text = mList[position].sizeSelected
        holder.textAmount.text = String.format("x%d", mList[position].amount)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


}