package com.example.term_project_wecollab
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.term_project_wecollab.databinding.ItemClothingBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ItemRecyclerViewAdapter(private val ItemModelList: ArrayList<Item>): RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClothingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("What is the position value? ", position.toString())
        // Get element from dataset at this position and replace
        // the contents of the view with that element
        holder.textViewPrice1.text = String.format("$%s", Util.moneyFormat(ItemModelList[position].price))
        holder.textViewDescription1.text = ItemModelList[position].name
        val bundle = Bundle()
        setImage(position, holder)
        bundle.putParcelable("item", ItemModelList[position])
        bundle.putStringArrayList("sizes", ItemModelList[position].sizes)


        val navHost = (holder.itemView.context as? AppCompatActivity)?.supportFragmentManager?.findFragmentById(R.id.fragmentContainerView_main)
        holder.imageView1.setOnClickListener {
            navHost?.findNavController()?.navigate(R.id.action_homeFragment_to_itemFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return ItemModelList.size
    }



    private fun setImage(position: Int, holder: ViewHolder) {
        val imageStorage = FirebaseStorage.getInstance()
        imageStorage.getReference(ItemModelList[position].image).downloadUrl.addOnSuccessListener {
            val imageUri = it.toString()
            Glide.with(holder.imageView1.context).load(imageUri).into(holder.imageView1)
        }
    }
    class ViewHolder(binding: ItemClothingBinding) : RecyclerView.ViewHolder(binding.root) {
        val textViewPrice1: TextView = binding.textViewItemPriceOne
        val imageView1: ImageView = binding.imageViewItemOne
        val textViewDescription1: TextView = binding.textViewDescriptionOne
    }
}