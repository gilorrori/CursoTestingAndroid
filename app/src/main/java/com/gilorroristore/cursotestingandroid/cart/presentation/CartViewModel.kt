package com.gilorroristore.cursotestingandroid.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.cart.domain.usecase.GetCartItemsWithPromotionsUseCase
import com.gilorroristore.cursotestingandroid.cart.domain.usecase.GetCartSummaryUseCase
import com.gilorroristore.cursotestingandroid.cart.domain.usecase.UpdateCartItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val getCartSummaryUseCase: GetCartSummaryUseCase,
    private val updateCardItemUseCase: UpdateCartItemUseCase,
    private val getCartItemsWithPromotionsUseCase: GetCartItemsWithPromotionsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CartEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<CartEvent> = _event

    var cartJob: Job? = null

    init {
        loadCart()
    }

    fun loadCart() {
        _uiState.value = CartUiState.Loading
        /* si el cartJob esta corriendo se cancela y se carga de nuevo*/
        cartJob?.cancel()

        cartJob = combine(
            getCartItemsWithPromotionsUseCase(),
            getCartSummaryUseCase()
        ) { cartItemsWithPromotion, summary ->
            _uiState.value = CartUiState.Success(
                summary = summary,
                cartItems = cartItemsWithPromotion,
                isLoading = false
            )
        }.catch {e ->
            _event.emit(CartEvent.ShowMessage(e.message.orEmpty()))
        }.launchIn(
            viewModelScope
        )
    }

    fun updateCartItem(productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                updateCardItemUseCase(productId, quantity)
            } catch (e: Exception) {
                _event.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            try {
                cartItemRepository.removeFromCart(productId)
            } catch (e: Exception) {
                _event.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun increaseQuantity(productId: String, currentQuantity: Int) {
        updateCartItem(productId, currentQuantity + 1)
    }

    fun decreaseQuantity(productId: String, currentQuantity: Int) {
        if (currentQuantity > 1) {
            updateCartItem(productId, currentQuantity - 1)
        } else {
            removeFromCart(productId)
        }
    }
}