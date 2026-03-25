package com.gilorroristore.cursotestingandroid.detail.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product

@Composable
fun AddToCardButtonWithStock(
    modifier: Modifier = Modifier,
    product: Product,
    isLoading: Boolean,
    addToCart: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        tonalElevation = 2.dp
    ) {
        Box(modifier.padding(16.dp)) {
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = { addToCart() },
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart, contentDescription = "Shopping Cart",
                    modifier = modifier.size(
                        20.dp
                    )
                )
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Agregar al carrito", fontWeight = FontWeight.Bold)
            }
        }
    }
}