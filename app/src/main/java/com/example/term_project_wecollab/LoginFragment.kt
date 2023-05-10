package com.example.term_project_wecollab

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.term_project_wecollab.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var thisView: View
    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
        val signupButton: Button = binding.buttonLoginSignupRedirect
        val loginButton: Button = binding.buttonLoginLoginRedirect
        val passwordField: TextInputLayout = binding.textFieldLoginPassword
        signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        loginButton.setOnClickListener {
            val email = binding.textFieldLoginEmail.editText?.text.toString()
            val password = binding.textFieldLoginPassword.editText?.text.toString()
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBarLogin.visibility = VISIBLE
                binding.linearLayoutLoginRoot.visibility = INVISIBLE
                val success = authenticationViewModel.login(email, password)
                println("VALUE OF SUCCESS: $success")
                if (success) {
                    startActivity(Intent(this@LoginFragment.context, MainActivity::class.java))
                } else {
                    binding.progressBarLogin.visibility = INVISIBLE
                    binding.linearLayoutLoginRoot.visibility = VISIBLE
                    passwordField.error = "Email or password is not correct."
                }
                binding.progressBarLogin.visibility = INVISIBLE
                binding.linearLayoutLoginRoot.visibility = VISIBLE
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
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}