package com.gilorroristore.cursotestingandroid.cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.gilorroristore.cursotestingandroid.cart.presentation.model.CartItemsWithPromotion
import com.gilorroristore.cursotestingandroid.core.presentation.components.MarketTopAppBar
import com.gilorroristore.cursotestingandroid.core.presentation.components.QuantitySelector
import java.text.NumberFormat
import java.util.Currency

@Composable
fun CartScreen(
    onBack: () -> Unit, cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        cartViewModel.events.collect { event ->
            when (event) {
                is CartEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { MarketTopAppBar("Carrito") { onBack() } }) { paddingValues ->

        when (val state = uiState) {
            CartUiState.Loading -> {
                CartLoadingStateScreen(modifier = Modifier.padding(paddingValues))
            }

            is CartUiState.Error -> {
                CartErrorStateScreen(
                    modifier = Modifier.padding(paddingValues),
                    state,
                    onRetrySelect = { cartViewModel.loadCart() })
            }

            is CartUiState.Success -> {
                CartSuccessStateScreen(
                    modifier = Modifier.padding(paddingValues),
                    state = state,
                    onIncreaseQuantity = { productId, quantity ->
                        cartViewModel.increaseQuantity(
                            productId = productId, currentQuantity = quantity
                        )
                    },
                    onDecreaseQuantity = { productId, quantity ->
                        cartViewModel.decreaseQuantity(
                            productId = productId, currentQuantity = quantity
                        )
                    })
            }
        }
    }
}

@Composable
fun CartSuccessStateScreen(
    modifier: Modifier = Modifier,
    state: CartUiState.Success,
    onIncreaseQuantity: (String, Int) -> Unit,
    onDecreaseQuantity: (String, Int) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
        ) {
        if (state.cartItems.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "🛒", style = MaterialTheme.typography.displayLarge)
                Text(
                    text = "Tu carrito esta vacío",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Agrega productos para comenzar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.cartItems) { itemWithProduct ->
                    CartItemCard(
                        itemWithProduct = itemWithProduct,
                        onIncreaseQuantity = { productId, quantity ->
                            onIncreaseQuantity(productId, quantity)
                        },
                        onDecreaseQuantity = { productId, quantity ->
                            onDecreaseQuantity(productId, quantity)
                        },
                        onRemove = {

                        })
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    itemWithProduct: CartItemsWithPromotion,
    onIncreaseQuantity: (String, Int) -> Unit,
    onDecreaseQuantity: (String, Int) -> Unit,
    onRemove: () -> Unit
) {
    val product = itemWithProduct.product
    val cartItem = itemWithProduct.cartItem

    // preparando la moneda local y que siempre lo recuerde sin generarlo cada que se recomponga
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance("USD")
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                modifier = Modifier.weight(1f),
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(3f)
            ) {
                Text(text = product.name)
                //Promo
                Text(text = "Total: ${currencyFormatter.format(product.price)}")
                QuantitySelector(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ),
                    quantity = cartItem.quantity.toString(),
                    canDecrease = cartItem.quantity > 1,
                    canIncrease = cartItem.quantity < product.stock,
                    onDecreaseSelected = { onDecreaseQuantity(product.id, cartItem.quantity) },
                    onIncreaseSelected = { onIncreaseQuantity(product.id, cartItem.quantity) })
            }
        }
    }
}


@Composable
fun CartErrorStateScreen(
    modifier: Modifier = Modifier, state: CartUiState.Error, onRetrySelect: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: ${state.message}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier.height(16.dp))

        Button(
            onClick = { onRetrySelect() }) {
            Text(text = "Reintentar")
        }
    }
}

@Composable
fun CartLoadingStateScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}