package com.gilorroristore.cursotestingandroid.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gilorroristore.cursotestingandroid.cart.domain.repositories.CartItemRepository
import com.gilorroristore.cursotestingandroid.cart.domain.usecase.AddToCartUseCase
import com.gilorroristore.cursotestingandroid.cart.domain.usecase.GetCartSummaryUseCase
import com.gilorroristore.cursotestingandroid.cart.domain.usecase.UpdateCartItemUseCase
import com.gilorroristore.cursotestingandroid.cart.presentation.model.CartItemsWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CartViewModel @Inject constructor(
    val cartItemRepository: CartItemRepository,
    val productRepository: ProductRepository,
    val getCartSummaryUseCase: GetCartSummaryUseCase,
    val addToCartUseCase: AddToCartUseCase,
    val updateCardItemUseCase: UpdateCartItemUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CartEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<CartEvent> = _events

    var cartJob: Job? = null

    init {
        loadCart()
    }

    fun loadCart() {
        _uiState.value =
            CartUiState.Loading/* si el cartJob esta corriendo se cancela y se carga de nuevo*/
        cartJob?.cancel()

        //flatMapLatest Si hay  hay un map lo cancela y devuelve a consumir y regresa el ultimo
        cartJob = cartItemRepository.getCardItems().flatMapLatest { cartItems ->
            val ids = cartItems.mapTo(mutableSetOf()) { it.productId }

            if (ids.isEmpty()) {
                getCartSummaryUseCase().map { summary ->
                    _uiState.value = CartUiState.Success(
                        summary = summary, cartItems = emptyList(), isLoading = false
                    )
                }
            } else {
                combine(
                    productRepository.getProductsByIds(ids), getCartSummaryUseCase()
                ) { products, summary ->

                    val productsById = products.associateBy { it.id }
                    val cartItemsWithProducts = cartItems.mapNotNull { cartItem ->
                        val finalProduct =
                            productsById[cartItem.productId] ?: return@mapNotNull null

                        CartItemsWithPromotion(
                            product = finalProduct, cartItem = cartItem
                        )
                    }

                    _uiState.value = CartUiState.Success(
                        summary = summary, cartItems = cartItemsWithProducts, isLoading = false
                    )
                }
            }
        }.catch { e: Throwable ->
            _uiState.value = CartUiState.Error(e.message.orEmpty())
        }.launchIn(viewModelScope)
    }

    fun updateCartItem(productId: String, quantity: Int) {
        viewModelScope.launch {
            try {
                updateCardItemUseCase(productId, quantity)
            } catch (e: Exception) {
                _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
            }
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            try {
                cartItemRepository.removeFromCart(productId)
            } catch (e: Exception) {
                _events.emit(CartEvent.ShowMessage(e.message.orEmpty()))
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