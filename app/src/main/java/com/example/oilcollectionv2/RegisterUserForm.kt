package com.example.oilcollectionv2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterUserForm(
    onRegisterSuccess: () -> Unit,
    onRegisterFailure: (String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var businessAddress by remember { mutableStateOf("") }
    var suburb by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var bankAccountNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Position") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = businessName,
            onValueChange = { businessName = it },
            label = { Text("Business Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = businessAddress,
            onValueChange = { businessAddress = it },
            label = { Text("Business Address") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = suburb,
            onValueChange = { suburb = it },
            label = { Text("Suburb") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = bankAccountNumber,
            onValueChange = { bankAccountNumber = it },
            label = { Text("Bank Account Number") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    val icon = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    val icon = if (isConfirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    fullName.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                        errorMessage = "Please fill out all required fields"
                    }
                    password != confirmPassword -> {
                        errorMessage = "Passwords do not match"
                    }
                    password.length < 6 -> {
                        errorMessage = "Password must be at least 6 characters"
                    }
                    else -> {
                        registerUser(
                            fullName = fullName,
                            position = position,
                            businessName = businessName,
                            businessAddress = businessAddress,
                            suburb = suburb,
                            city = city,
                            bankAccountNumber = bankAccountNumber,
                            email = email,
                            password = password,
                            onSuccess = onRegisterSuccess,
                            onFailure = onRegisterFailure
                        )
                    }
                }
            }
        ) {
            Text("Register")
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


fun registerUser(
    fullName: String,
    position: String,
    businessName: String,
    businessAddress: String,
    suburb: String,
    city: String,
    bankAccountNumber: String,
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val userData = hashMapOf(
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
                        .document(userId).set(userData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e.message ?: "Failed to save user data")
                        }
                } else {
                    onFailure("User ID is null")
                }
            } else {
                onFailure(task.exception?.message ?: "Registration failed")
            }
        }
}

@Composable
@Preview(showBackground = true)
fun RegisterFormPreview() {
    RegisterUserForm(
        onRegisterSuccess = { /* Do something on success */ },
        onRegisterFailure = { /* Handle failure */ }
    )
}

