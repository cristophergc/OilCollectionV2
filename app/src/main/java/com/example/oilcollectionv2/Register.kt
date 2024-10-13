package com.example.oilcollectionv2


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

fun handleRegister(
    email: String,
    password: String,
    navigateToLogin: () -> Unit,
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
        task ->
            if (task.isSuccessful) {
                navigateToLogin()
            } else {
                Log.e("error", task.exception?.message.toString())
            }
        }
}

@Composable
fun RegisterScreenUi(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    confirmPassword: String,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    errorMessage: String
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your image resource
            contentDescription = "Login Image",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email input
        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password input
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
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

        // Register button
        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        // Display error message if login fails
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun RegisterScreen(navigateToLogin: () -> Unit) {
    // State to hold user input
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    RegisterScreenUi(
        email = email,
        onEmailChange = { email = it },
        password = password,
        confirmPassword = confirmPassword,
        onPasswordChange = { password = it },
        onConfirmPasswordChange = {confirmPassword = it},
        onRegisterClick = {
            handleRegister(email, password, navigateToLogin)
        },
        errorMessage = errorMessage
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    RegisterScreen(navigateToLogin = {})
}
