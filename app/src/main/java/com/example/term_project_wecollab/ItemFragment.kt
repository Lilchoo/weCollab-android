package com.example.term_project_wecollab


import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.term_project_wecollab.databinding.FragmentItemBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File


/**
 * A simple [Fragment] subclass.
 * Use the [ItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemFragment : Fragment() {
    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!
    private var selectedSize: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val size = arguments?.getStringArrayList("sizes")
        val spinner = size?.let { setUpSpinner(it) }
        spinner?.let { getSpinnerSelectedListener(it, size) }

        val imageView = binding.imageViewItemProductImage
        val textName = binding.textViewItemProductName
        val textPrice = binding.textViewItemProductPrice
        val textDesc = binding.textViewItemProductDesc

        val item = arguments?.getParcelable("item", Item::class.java)
        val name = item?.name
        val price = item?.price
        val desc = item?.desc

        val imageStorage = FirebaseStorage.getInstance()
        val imageRef = item?.let { imageStorage.getReference(it.image) }
        val localFile = File.createTempFile("tempfile", "png")
        imageRef?.getFile(localFile)?.addOnSuccessListener {
            imageView.setImageURI(Uri.fromFile(localFile))
            localFile.delete()
        }

        textName.text = name
        textPrice.text = "$${price}"
        textDesc.text = desc

        val addCartButton = binding.buttonItemCart
        val userViewModel: UserViewModel by activityViewModels()
        val authenticationViewModel: AuthenticationViewModel by activityViewModels()


        addCartButton.setOnClickListener {
            if (item != null) {
                item.sizeSelected = selectedSize.toString()
                val currentUser = authenticationViewModel.currentUser()
                if (currentUser != null) {
                    userViewModel.addToCart(
                        currentUser.uid,
                        User.Purchase.PurchaseItem(item.itemId, 1, item.sizeSelected)
                    )
                }
            }
            findNavController().navigate(R.id.action_itemFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSpinner(size: ArrayList<String>): Spinner {
        val mySpinner = binding.spinnerItemSizecontainer
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, size)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = adapter
        return mySpinner
    }

    private fun getSpinnerSelectedListener(mySpinner: Spinner, size: ArrayList<String>){
        mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSize = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSize = size[0]
            }
        }

    }


}