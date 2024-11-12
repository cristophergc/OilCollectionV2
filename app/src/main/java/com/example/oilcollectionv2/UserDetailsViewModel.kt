package com.example.oilcollectionv2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserDetailsViewModel : ViewModel() {

    private val _userDetails = MutableStateFlow(UserDetails())
    val userDetails: StateFlow<UserDetails> = _userDetails

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun updateFullName(newFullName: String) {
        _userDetails.value = _userDetails.value.copy(fullName = newFullName)
    }

    fun updatePosition(newPosition: String) {
        _userDetails.value = _userDetails.value.copy(position = newPosition)
    }

    fun updateBusinessName(newBusinessName: String) {
        _userDetails.value = _userDetails.value.copy(businessName = newBusinessName)
    }

    fun updateBusinessAddress(newBusinessAddress: String) {
        _userDetails.value = _userDetails.value.copy(businessAddress = newBusinessAddress)
    }

    fun updateSuburb(newSuburb: String) {
        _userDetails.value = _userDetails.value.copy(suburb = newSuburb)
    }

    fun updateCity(newCity: String) {
        _userDetails.value = _userDetails.value.copy(city = newCity)
    }

    fun updateBankAccountNumber(newBankAccountNumber: String) {
        _userDetails.value = _userDetails.value.copy(bankAccountNumber = newBankAccountNumber)
    }

    fun getUserDetails() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val documentRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            _loading.value = true

            viewModelScope.launch {
                documentRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val userData = document.data?.let {
                                UserDetails(
                                    fullName = it["fullName"] as? String ?: "",
                                    position = it["position"] as? String ?: "",
                                    businessName = it["businessName"] as? String ?: "",
                                    businessAddress = it["businessAddress"] as? String ?: "",
                                    suburb = it["suburb"] as? String ?: "",
                                    city = it["city"] as? String ?: "",
                                    bankAccountNumber = it["bankAccountNumber"] as? String ?: "",
                                )
                            }
                            if (userData != null) {
                                _userDetails.value = userData
                                _loading.value = false
                            } else {
                                _errorMessage.value = "User data not found"
                                _loading.value = false
                            }
                        } else {
                            _errorMessage.value = "User data not found"
                            _loading.value = false
                        }
                    }
                    .addOnFailureListener { e ->
                        _loading.value = false
                        _errorMessage.value = e.message ?: "Failed to retrieve user document"
                    }
            }
        } else {
            _errorMessage.value = "User ID is null"
            _loading.value = false
            Log.e("TAG", "Failed to fetch user details: User ID is null")
        }
    }

    fun updateUserDetails() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val updatedUserData = hashMapOf(
                "fullName" to _userDetails.value.fullName,
                "position" to _userDetails.value.position,
                "businessName" to _userDetails.value.businessName,
                "businessAddress" to _userDetails.value.businessAddress,
                "suburb" to _userDetails.value.suburb,
                "city" to _userDetails.value.city,
                "bankAccountNumber" to _userDetails.value.bankAccountNumber,
            )

            _loading.value = true

            viewModelScope.launch {
                val documentRef = FirebaseFirestore.getInstance().collection("users").document(userId)

                documentRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            documentRef.update(updatedUserData as Map<String, Any>)
                                .addOnSuccessListener {
                                    _loading.value = false
                                }
                                .addOnFailureListener { e ->
                                    _loading.value = false
                                    _errorMessage.value = e.message ?: "Failed to update user data"
                                }
                        } else {
                            documentRef.set(updatedUserData)
                                .addOnSuccessListener {
                                    _loading.value = false
                                    _userDetails.value = UserDetails() // Reset fields after creation
                                }
                                .addOnFailureListener { e ->
                                    _loading.value = false
                                    _errorMessage.value = e.message ?: "Failed to create user data"
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        _loading.value = false
                        _errorMessage.value = e.message ?: "Failed to retrieve user document"
                    }
            }
        } else {
            _errorMessage.value = "User ID is null"
            _loading.value = false
            Log.e("TAG", "Failed to update user: User ID is null")
        }
    }
}
