package com.example.oilcollectionv2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun login(
        email: String,
        password: String,
        onLoginSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                checkUserDetailsFilled(onLoginSuccess)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Login failed"
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun checkUserDetailsFilled(
        onLoginSuccess: () -> Unit
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            try {
                val document = FirebaseFirestore.getInstance().collection("users")
                    .document(user.uid)
                    .get()
                    .await()

                if (document.exists()) {
                    onLoginSuccess()
                } else {
                    _errorMessage.value = "User details document not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to retrieve user details"
            }
        } else {
            _errorMessage.value = "User is not authenticated"
        }
    }


    fun clearError() {
        _errorMessage.value = ""
    }
}
