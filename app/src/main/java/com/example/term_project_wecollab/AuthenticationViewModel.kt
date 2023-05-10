package com.example.term_project_wecollab

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    suspend fun signUp(name: String, email: String, password: String) : Boolean {
        val success = CoroutineScope(Dispatchers.IO).async {
            auth.createUserWithEmailAndPassword(email, password).await()
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val user = User(name)
                userRepository.createUser(currentUser.uid, user)
                return@async true
            }
            return@async false
        }.await()
        return success
    }

    suspend fun login(email: String, password: String) : Boolean {
        val result = CoroutineScope(Dispatchers.IO).async {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                return@async false
            }
            return@async true
        }.await()
        return result
    }

    fun signOut() : Boolean {
        return if (userLoggedIn()) {
            auth.signOut()
            Log.d(SIGN_OUT, "signOutUser: Success")
            true
        } else {
            Log.d(SIGN_OUT, "signOutUser: Fail")
            false
        }
    }

    fun currentUser() : FirebaseUser? {
        return auth.currentUser
    }

    private fun userLoggedIn() : Boolean {
        return auth.currentUser != null
    }
}