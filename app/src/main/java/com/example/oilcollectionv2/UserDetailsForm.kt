package com.example.oilcollectionv2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//setup firestore, fix the navigation

@Composable
fun UserDetailsForm(
    onUpdateSuccess: () -> Unit,
    onUpdateFailure: (String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("") }
    var suburb by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var bankAccountNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") }
        )

        TextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Position") }
        )

        TextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = { Text("Business Name") }
        )

        TextField(
            value = businessAddress,
            onValueChange = { businessAddress = it },
            label = { Text("Business Address") }
        )

        TextField(
            value = suburb,
            onValueChange = { suburb = it },
            label = { Text("Suburb") }
        )

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") }
        )

        TextField(
            value = bankAccountNumber,
            onValueChange = { bankAccountNumber = it },
            label = { Text("Bank Account Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                updateUserDetails(
                    fullName = fullName,
                    position = position,
                    businessName = businessName,
                    businessAddress = businessAddress,
                    suburb = suburb,
                    city = city,
                    bankAccountNumber = bankAccountNumber,
                    email = email,
                    onSuccess = onUpdateSuccess,
                    onFailure = onUpdateFailure
                )
            }
        ) {
            Text("Update Details")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

fun updateUserDetails(
    fullName: String,
    position: String,
    businessName: String,
    businessAddress: String,
    suburb: String,
    city: String,
    bankAccountNumber: String,
    email: String,
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
            "email" to email
        )
        FirebaseFirestore.getInstance().collection("users")
            .document(userId).update(updatedUserData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Failed to update user data")
            }
    } else {
        onFailure("User ID is null")
    }
}

@Composable
@Preview(showBackground = true)
fun UpdateUserDetailsFormPreview() {
    UserDetailsForm(
        onUpdateSuccess = { },
        onUpdateFailure = { }
    )
}