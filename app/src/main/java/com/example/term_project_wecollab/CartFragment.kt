package com.example.term_project_wecollab

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.term_project_wecollab.databinding.FragmentCartBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    private var param1: Item? = null
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1, Item::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val purchaseButton = binding.buttonCartPurchase
        val clearButton = binding.buttonCartClearCart

        CoroutineScope(Dispatchers.Main).launch {
            val currentUser = authenticationViewModel.currentUser()
            if (currentUser != null) {
                val items = userViewModel.getCart(currentUser.uid)
                setupRecyclerView(items)
            }
        }

        purchaseButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val currentUser = authenticationViewModel.currentUser() ?: return@launch
                val cartList = userViewModel.getCart(currentUser.uid)
                if (cartList.size == 0) {
                    Snackbar.make(view, "No Item in the cart", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        val uid = authenticationViewModel.currentUser()?.uid
                        if (uid != null) {
                            userViewModel.addPurchase(uid, cartList)
                            userViewModel.removeCart(uid)
                        }
                    }
                    Snackbar.make(view, "Purchase Completed", Snackbar.LENGTH_SHORT)
                        .show()
                    userViewModel.removeCart(currentUser.uid)
                    findNavController().navigate(R.id.action_cartFragment_to_homeFragment)
                }
            }
        }

        clearButton.setOnClickListener {
            val currentUser = authenticationViewModel.currentUser() ?: return@setOnClickListener
            userViewModel.removeCart(currentUser.uid)
            Snackbar.make(view, "Cart Cleared!", Snackbar.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_cartFragment_self)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(itemList: ArrayList<Item>) {
        val recycler = binding.recyclerViewCart
        recycler.adapter = CartItemAdapter(itemList)
        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment CartFragment.
         */
        @JvmStatic
        fun newInstance(param1: Item) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}