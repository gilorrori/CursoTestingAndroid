package com.gilorroristore.cursotestingandroid.productlist.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor() : ViewModel() {

    //Inicializando nuestro state de la screen
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    // asStateFlow es usado para proteger el _uiState de modificarlo por fuera mediante (uiState as Mutablestateflow).valie = ...
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    /* para que si se lanza un evento antes de que se este escuchando, no se pierda *
    * Se ocupa el Shared debido a que son eventos que ocuren UNA UNICA VEZ.
    * */
    private val _events = MutableSharedFlow<ProductListEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ProductListEvent> = _events


    init {
        loadProducts()
    }

    private fun loadProducts() {
        _uiState.value = ProductListUiState.Loading
    }

}