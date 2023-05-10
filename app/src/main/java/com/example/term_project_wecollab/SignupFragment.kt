package com.example.term_project_wecollab

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.term_project_wecollab.databinding.FragmentSignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton: Button = binding.buttonSignupLoginRedirect
        val signupButton: Button = binding.buttonSignupSignupRedirect
        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
        signupButton.setOnClickListener {
            val name = binding.textFieldSignupName.editText?.text.toString()
            val email = binding.textFieldSignupEmail.editText?.text.toString()
            val password = binding.textFieldSignupPassword.editText?.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBarSignIn.visibility = VISIBLE
                binding.linearLayoutSignInRoot.visibility = INVISIBLE
                val success = authenticationViewModel.signUp(name, email, password)
                if (success) {
                    startActivity(Intent(this@SignupFragment.context, MainActivity::class.java))
                }
                binding.progressBarSignIn.visibility = INVISIBLE
                binding.linearLayoutSignInRoot.visibility = VISIBLE
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
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = SignupFragment()
    }
}