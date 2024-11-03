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
                value = viewModel.fullName,
                onValueChange = { viewModel.fullName = it },
                label = { Text("Full Name") }
            )

            TextField(
                value = viewModel.position,
                onValueChange = { viewModel.position = it },
                label = { Text("Position") }
            )

            TextField(
                value = viewModel.businessName,
                onValueChange = { viewModel.businessName = it },
                label = { Text("Business Name") }
            )

            TextField(
                value = viewModel.businessAddress,
                onValueChange = { viewModel.businessAddress = it },
                label = { Text("Business Address") }
            )

            TextField(
                value = viewModel.suburb,
                onValueChange = { viewModel.suburb = it },
                label = { Text("Suburb") }
            )

            TextField(
                value = viewModel.city,
                onValueChange = { viewModel.city = it },
                label = { Text("City") }
            )

            TextField(
                value = viewModel.bankAccountNumber,
                onValueChange = { viewModel.bankAccountNumber = it },
                label = { Text("Bank Account Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateUserDetails(
                        onSuccess = onUpdateSuccess,
                        onFailure = onUpdateFailure
                    )
                },
                enabled = !viewModel.loading
            ) {
                Text("Update Details")
            }

            if (viewModel.errorMessage.isNotEmpty()) {
                Text(
                    text = viewModel.errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (viewModel.loading) {
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
