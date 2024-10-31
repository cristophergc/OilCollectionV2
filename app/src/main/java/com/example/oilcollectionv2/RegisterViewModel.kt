package com.example.oilcollectionv2

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _emailError = MutableStateFlow("")
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow("")
    val passwordError = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow("")
    val confirmPasswordError = _confirmPasswordError.asStateFlow()

    private val _isRegistrationSuccessful = MutableStateFlow(false)
    val isRegistrationSuccessful = _isRegistrationSuccessful.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        validateEmail(newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        validatePassword(newPassword)
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        validateConfirmPassword(newConfirmPassword)
    }

    private fun validateEmail(email: String) {
        if (email.isEmpty()) {
            _emailError.value = "Please add a valid email"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Invalid email format"
        } else {
            _emailError.value = ""
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            _passwordError.value = "Password cannot be empty"
        } else if (password.length < 6) {
            _passwordError.value = "Password must be at least 6 characters"
        } else {
            _passwordError.value = ""
        }
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        if (confirmPassword.isEmpty()) {
            _confirmPasswordError.value = "Please confirm your password"
        } else if (confirmPassword != _password.value) {
            _confirmPasswordError.value = "Passwords do not match"
        } else {
            _confirmPasswordError.value = ""
        }
    }

    fun register(navigateToUserDetailsForm: () -> Unit) {

        validateEmail(_email.value)
        validatePassword(_password.value)
        validateConfirmPassword(_confirmPassword.value)

        if (_emailError.value.isEmpty() && _passwordError.value.isEmpty() && _confirmPasswordError.value.isEmpty()) {
            viewModelScope.launch {
                _loading.value = true
                firebaseAuth.createUserWithEmailAndPassword(_email.value, _password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _isRegistrationSuccessful.value = true
                            navigateToUserDetailsForm()
                        } else {
                            // TODO: handle the error
                        }
                        _loading.value = false
                    }
            }
        }
    }
}
