package com.gilorroristore.cursotestingandroid.detail.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddToCardButtonNoStock(modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Box(modifier.padding(16.dp)) {
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = { },
                enabled = false,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Shopping Cart",
                    modifier = modifier.size(
                        20.dp
                    )
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Sin stock disponible", fontWeight = FontWeight.Bold)
            }
        }
    }
}