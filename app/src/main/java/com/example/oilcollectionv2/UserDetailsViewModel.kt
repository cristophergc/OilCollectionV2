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
