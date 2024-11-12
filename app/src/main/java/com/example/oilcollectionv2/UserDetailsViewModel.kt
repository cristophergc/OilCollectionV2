package com.example.oilcollectionv2

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserDetailsViewModel : ViewModel() {

    var fullName by mutableStateOf("")
    var position by mutableStateOf("")
    var businessName by mutableStateOf("")
    var businessAddress by mutableStateOf("")
    var suburb by mutableStateOf("")
    var city by mutableStateOf("")
    var bankAccountNumber by mutableStateOf("")
    var email by mutableStateOf("")

    var errorMessage by mutableStateOf("")
    var loading by mutableStateOf(false)

    fun getUserDetails(onSuccess: (HashMap<String, Any>) -> Unit, onFailure: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val documentRef = FirebaseFirestore.getInstance().collection("users").document(userId)
            loading = true

            viewModelScope.launch {
                documentRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val userData = document.data as HashMap<String, Any>
                            fullName = userData["fullName"] as String
                            position = userData["position"] as String
                            businessName = userData["businessName"] as String
                            businessAddress = userData["businessAddress"] as String
                            suburb = userData["suburb"] as String
                            city = userData["city"] as String
                            bankAccountNumber = userData["bankAccountNumber"] as String
                            email = userData["email"] as String
                            loading = false
                            onSuccess(userData)
                        } else {
                            loading = false
                            onFailure("User data not found")
                        }
                    }
                    .addOnFailureListener { e ->
                        loading = false
                        onFailure(e.message ?: "Failed to retrieve user document")
                    }
            }
        } else {
            onFailure("User ID is null")
            Log.e("TAG", "Failed to fetch user details: User ID is null")
        }
    }

    fun updateUserDetails(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val updatedUserData: HashMap<String, Any> = hashMapOf(
                "fullName" to fullName,
                "position" to position,
                "businessName" to businessName,
                "businessAddress" to businessAddress,
                "suburb" to suburb,
                "city" to city,
                "bankAccountNumber" to bankAccountNumber,
                "email" to email,
            )

            loading = true

            viewModelScope.launch {
                val documentRef =
                    FirebaseFirestore.getInstance().collection("users").document(userId)

                documentRef.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            documentRef.update(updatedUserData)
                                .addOnSuccessListener {
                                    loading = false
                                    fullName = ""
                                    position = ""
                                    businessName = ""
                                    businessAddress = ""
                                    suburb = ""
                                    city = ""
                                    bankAccountNumber = ""
                                    email = ""
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    loading = false
                                    onFailure(
                                        e.message ?: "Failed to update user data"
                                    )
                                }
                        } else {
                            documentRef.set(updatedUserData)
                                .addOnSuccessListener {
                                    loading = false
                                    fullName = ""
                                    position = ""
                                    businessName = ""
                                    businessAddress = ""
                                    suburb = ""
                                    city = ""
                                    bankAccountNumber = ""
                                    email = ""
                                    onSuccess()
                                }
                                .addOnFailureListener { e ->
                                    loading = false
                                    onFailure(
                                        e.message ?: "Failed to create user data"
                                    )
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.message ?: "Failed to retrieve user document")
                    }
            }
        } else {
            onFailure("User ID is null")
            Log.e("TAG", "Failed to update user: User ID is null")
        }
    }
}
