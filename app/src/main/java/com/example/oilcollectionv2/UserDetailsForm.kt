package com.example.oilcollectionv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserDetailsForm(
    viewModel: UserDetailsViewModel = viewModel(),
    onUpdateSuccess: () -> Unit,
    onUpdateFailure: (String) -> Unit
) {
    val userDetails by viewModel.userDetails.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = userDetails.fullName,
                onValueChange = { viewModel.updateFullName(it) },
                label = { Text("Full Name") }
            )

            TextField(
                value = userDetails.position,
                onValueChange = { viewModel.updatePosition(it) },
                label = { Text("Position") }
            )

            TextField(
                value = userDetails.businessName,
                onValueChange = { viewModel.updateBusinessName(it) },
                label = { Text("Business Name") }
            )

            TextField(
                value = userDetails.businessAddress,
                onValueChange = { viewModel.updateBusinessAddress(it) },
                label = { Text("Business Address") }
            )

            TextField(
                value = userDetails.suburb,
                onValueChange = { viewModel.updateSuburb(it) },
                label = { Text("Suburb") }
            )

            TextField(
                value = userDetails.city,
                onValueChange = { viewModel.updateCity(it) },
                label = { Text("City") }
            )

            TextField(
                value = userDetails.bankAccountNumber,
                onValueChange = { viewModel.updateBankAccountNumber(it) },
                label = { Text("Bank Account Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateUserDetails()
                    if (errorMessage.isEmpty()) {
                        onUpdateSuccess()
                    } else {
                        onUpdateFailure(errorMessage)
                    }
                },
                enabled = !loading
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

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UpdateUserDetailsFormPreview() {
    UserDetailsForm(
        viewModel = UserDetailsViewModel(),
        onUpdateSuccess = {},
        onUpdateFailure = {}
    )
}
