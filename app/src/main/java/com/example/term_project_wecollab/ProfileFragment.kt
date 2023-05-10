package com.example.term_project_wecollab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.term_project_wecollab.databinding.FragmentProfileBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private lateinit var thisView: View
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        thisView = view
        CoroutineScope(Dispatchers.Main).launch {
            val uid = authenticationViewModel.currentUser()?.uid
            if (uid != null) {
                val userDoc = userViewModel.getUserInfo(uid).await()
                val user = userDoc.toObject(User::class.java)
                if (user != null)  {
                    binding.textViewProfileName.text = user.name
                }
            }
        }

        binding.buttonProfileEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val uid = authenticationViewModel.currentUser()?.uid
            if (uid != null) {
                val userDoc = userViewModel.getUserInfo(uid).await()
                val user = userDoc.toObject(User::class.java)
                if (user != null) {
                    val purchases = user.purchases
                    val purchaseHistoryItemAdapter = PurchaseHistoryItemAdapter(purchases) {
                        findNavController()
                    }
                    val recyclerView = binding.recyclerViewPurchases
                    val divider = MaterialDividerItemDecoration(thisView.context, LinearLayoutManager.VERTICAL)
                    recyclerView.addItemDecoration(divider)
                    recyclerView.layoutManager = LinearLayoutManager(this@ProfileFragment.context)
                    recyclerView.adapter = purchaseHistoryItemAdapter
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
         * @return A new instance of fragment ProfileFragment.
         */
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}