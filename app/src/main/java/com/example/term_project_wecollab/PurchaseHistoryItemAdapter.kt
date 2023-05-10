package com.example.term_project_wecollab

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.util.Supplier
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.term_project_wecollab.databinding.FragmentPurchaseHistoryItemBinding
import java.text.DecimalFormat


class PurchaseHistoryItemAdapter(private val purchases: List<User.Purchase>, private val callback: Supplier<NavController>) :
    RecyclerView.Adapter<PurchaseHistoryItemAdapter.ViewHolder>() {

    // Holds the views
    class ViewHolder(binding: FragmentPurchaseHistoryItemBinding) : RecyclerView.ViewHolder(
        binding.root) {
        val orderNumber: TextView = binding.textViewPurchaseItemOrderNumber
        val purchaseDate: TextView = binding.textViewPurchaseItemPurchaseDate
        val totalItems: TextView = binding.textViewPurchaseItemTotaltems
        val totalPrice: TextView = binding.textViewPurchaseItemTotalPrice
        val root: LinearLayout = binding.linearLayoutPurchaseHistoryRoot
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = FragmentPurchaseHistoryItemBinding.inflate(LayoutInflater.from(parent.context)
            , parent, false)
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        holder.orderNumber.text = purchases[position].orderNumber
        holder.purchaseDate.text = purchases[position].purchaseDate
        holder.totalItems.text = purchases[position].totalItems.toString()

        val totalPrice = String.format("$%s", Util.moneyFormat(purchases[position].totalPrice))
        holder.totalPrice.text = totalPrice
        holder.root.setOnClickListener {
            val bundle = bundleOf("orderNumber" to holder.orderNumber.text.toString())
            callback.get().navigate(R.id.action_profileFragment_to_purchaseHistoryItemDetailFragment, bundle)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return purchases.size
    }

}