package com.example.oilcollectionv2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmationScreen(onOkButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your collection has been requested",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = { onOkButtonClick() },
            modifier = Modifier.size(300.dp, 60.dp)
        ) {
            Text(
                text = "Ok",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationScreenPreview() {
    ConfirmationScreen(onOkButtonClick = {})
}
