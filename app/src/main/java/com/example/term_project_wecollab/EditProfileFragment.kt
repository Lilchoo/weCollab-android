package com.example.term_project_wecollab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.term_project_wecollab.databinding.FragmentEditProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {

    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var thisView: View
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view

        CoroutineScope(Dispatchers.Main).launch {
            val uid = authenticationViewModel.currentUser()?.uid
            if (uid != null) {
                val userDoc = userViewModel.getUserInfo(uid).await()
                val user = userDoc.toObject(User::class.java)
                if (user != null) {
                    val editNameField = binding.textFieldEditProfileName
                    editNameField.editText?.setText(user.name)
                }
            }
        }
        binding.buttonEditProfileCancel.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
        binding.buttonEditProfileConfirm.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val uid = authenticationViewModel.currentUser()?.uid
                if (uid != null) {
                    val name = binding.textFieldEditProfileName.editText?.text.toString()
                    userViewModel.updateName(uid, name)
                }
            }
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
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
         * @return A new instance of fragment EditProfileFragment.
         */
        @JvmStatic
        fun newInstance() = EditProfileFragment()
    }
}