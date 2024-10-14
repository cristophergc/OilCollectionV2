package com.example.oilcollectionv2


import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextDecoration
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
    navigateToLogin: () -> Unit,
    emailError: String,
    passwordError: String,
    confirmPasswordError: String
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

        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty()
        )

        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            isError = passwordError.isNotEmpty()
        )

        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            isError = confirmPasswordError.isNotEmpty()
        )

        if (confirmPasswordError.isNotEmpty()) {
            Text(
                text = confirmPasswordError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account? Login",
            style = MaterialTheme.typography.bodySmall,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { navigateToLogin() }
        )
    }
}


@Composable
fun RegisterScreen(navigateToLogin: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    fun validateInputs(): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailError = "Please add a valid email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
            isValid = false
        } else {
            emailError = ""
        }

        if (password.isEmpty()) {
            passwordError = "Password cannot be empty"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError = ""
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        } else {
            confirmPasswordError = ""
        }

        return isValid
    }

    RegisterScreenUi(
        email = email,
        onEmailChange = { email = it },
        password = password,
        confirmPassword = confirmPassword,
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegisterClick = {
            if (validateInputs()) {
                handleRegister(email, password, navigateToLogin)
            }
        },
        navigateToLogin = navigateToLogin,
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navigateToLogin = {})
}
