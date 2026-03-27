package com.gilorroristore.cursotestingandroid.cart.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gilorroristore.cursotestingandroid.core.presentation.components.MarketTopAppBar

@Composable
fun CartScreen(
    onBack: () -> Unit, cartViewModel: CartViewModel = hiltViewModel()
) {
    val uiState = cartViewModel.uiState.collectAsStateWithLifecycle()
    val snackBar = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        cartViewModel.events.collect { event ->
            when (event) {
                is CartEvent.ShowMessage -> snackBar.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        topBar = {
            MarketTopAppBar("Carrito") {
                onBack()
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = "Pantalla del carrito")
        }
    }
}