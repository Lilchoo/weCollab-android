package com.example.term_project_wecollab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.term_project_wecollab.databinding.FragmentPurchaseHistoryItemDetailBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PurchaseHistoryItemDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PurchaseHistoryItemDetailFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private val itemViewModel: ItemViewModel by activityViewModels()
    private var _binding: FragmentPurchaseHistoryItemDetailBinding? = null
    private val binding get() = _binding!!


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentPurchaseHistoryItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            val uid = authenticationViewModel.currentUser()?.uid
            val orderNumber = arguments?.getString("orderNumber")
            if (uid != null && orderNumber != null) {
                val purchase = userViewModel.getPurchase(uid, orderNumber)
                if (purchase != null) {

                    binding.textViewPurchaseDetailOrderNumber.text = purchase.orderNumber
                    binding.textViewPurchaseDetailPurchaseDate.text = purchase.purchaseDate
                    binding.textViewPurchaseDetailTotal.text = String.format("$%.2f", purchase.totalPrice)

                    val purchaseHistoryItemDetailAdapter = PurchaseHistoryItemDetailAdapter(
                        purchase.items, itemViewModel)
                    val recyclerView = binding.recyclerViewPurchaseHistoryItemDetailItems
                    val divider = MaterialDividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
                    recyclerView.addItemDecoration(divider)
                    recyclerView.layoutManager = LinearLayoutManager(this@PurchaseHistoryItemDetailFragment.context)
                    recyclerView.adapter = purchaseHistoryItemDetailAdapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PurchaseHistoryItemDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PurchaseHistoryItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}