package com.gilorroristore.cursotestingandroid.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gilorroristore.cursotestingandroid.detail.domain.usecase.GetProductDetailWithPromotionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailWithPromotionUseCase: GetProductDetailWithPromotionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ProductDetailEvent>(extraBufferCapacity = 1)
    val event: SharedFlow<ProductDetailEvent> = _event.asSharedFlow()

    private var productJob: Job? = null

    fun loadProduct(id: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        // si se tenia algun productjob corriendo, entonces se cancela y se empieza de nuevo
        productJob?.cancel()

        productJob = getProductDetailWithPromotionUseCase(id)
            .onEach { productWithPromotion ->
                _uiState.value = _uiState.value.copy(item = productWithPromotion, isLoading = false)
            }
            .catch { e: Throwable ->
                _uiState.value = _uiState.value.copy(isLoading = false)
                _event.emit(ProductDetailEvent.ShowError(e.message.orEmpty()))

            }
            .launchIn(viewModelScope)
    }

    fun addToCart() {

    }
}