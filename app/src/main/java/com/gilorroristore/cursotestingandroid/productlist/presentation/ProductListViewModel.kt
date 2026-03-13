package com.gilorroristore.cursotestingandroid.productlist.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

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

        getProductsUseCase()
            // Cada vez que devuelva un flow
            .onEach { products: List<Product> ->
                _uiState.value = ProductListUiState.Success(products)
            }.catch { e: Throwable ->
                _uiState.value = ProductListUiState.Error(e.message.orEmpty())
            }
            //Ejecutate en scope del vm para cuando el vm muera, esto también.
            .launchIn(viewModelScope)


    }

}