package com.gilorroristore.cursotestingandroid.productlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.ProductWithPromotion
import com.gilorroristore.cursotestingandroid.productlist.domain.models.SortOption
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.SettingsRepository
import com.gilorroristore.cursotestingandroid.productlist.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    //Inicializando nuestro state de la screen
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)

    // asStateFlow es usado para proteger el _uiState de modificarlo por fuera mediante (uiState as Mutablestateflow).value = ...
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    /* para que si se lanza un evento antes de que se este escuchando, no se pierda *
    * Se ocupa el Shared debido a que son eventos que ocuren UNA UNICA VEZ.
    * extraBufferCapacity permite almacenar el evento asignado (1) hasta que alguien escuche este evento
    * */
    private val _events = MutableSharedFlow<ProductListEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<ProductListEvent> = _events

    /*Statein para ejecutar esa corrutina */
    val filterVisible: StateFlow<Boolean> = settingsRepository.filtersVisible.stateIn(
        scope = viewModelScope,/* Valor inicial por sino se encuentra o tarda */
        initialValue = true,/* siempre escucha mientras haya alguien suscrito (screen) pero sino hay nadie suscrito en 5s deja de observar y muere */
        started = SharingStarted.WhileSubscribed(5000)
    )

    /* Es una corrutina que se esta lanzando*/
    private var productsJobs: Job? = null

    init {
        loadProducts()
    }

    private fun loadProducts() {
        _uiState.value = ProductListUiState.Loading
        productsJobs?.cancel()

        productsJobs = combine(
            getProductsUseCase(),
            settingsRepository.selectedCategory,
            settingsRepository.sortOption
        ) { products, category, sortOption ->
            var filterProducts = products
            if (category != null) {
                filterProducts = filterProducts.filter { it.product.category == category }
            }

            val sorted = when (sortOption) {
                SortOption.PRICE_ASC -> filterProducts.sortedBy { effectivePrice(it) }
                SortOption.PRICE_DESC -> filterProducts.sortedByDescending { effectivePrice(it) }
                SortOption.NONE -> filterProducts
                SortOption.DISCOUNT ->
                    //filterProducts.sortedByDescending { effectiveDiscountPercent(it) }
                    filterProducts.sortedWith(compareByDescending<ProductWithPromotion> {
                        effectiveDiscountPercent(it)
                    }.thenBy { it.promotion == null })
            }

            /*
          se recorre cada producto y se obtiene la categoria siempre y cuando sean
          distintos y ademas ordenados */
            val categories = products.map { it.product.category }.distinct().sorted()

            ProductListUiState.Success(
                products = sorted,
                categories = categories,
                selectedCategory = category,
                sortOption = sortOption
            )
        }.onEach { state ->
            // Cada vez que devuelva un flow
            _uiState.value = state
        }.catch { e: Throwable ->
            _uiState.value = ProductListUiState.Error(e.message.orEmpty())
        } //Ejecutate en scope del vm para cuando el vm muera, esto también.
            .launchIn(viewModelScope)
    }

    fun setCategory(category: String?) {
        viewModelScope.launch {
            settingsRepository.setSelectedCategory(category)
        }
    }

    fun setSortOption(sortOption: SortOption) {
        viewModelScope.launch {
            settingsRepository.setSortOption(sortOption)
        }
    }

    fun setFilterVisible(showFilters: Boolean) {
        viewModelScope.launch {
            settingsRepository.setFiltersVisible(showFilters)
        }
    }

    private fun effectiveDiscountPercent(item: ProductWithPromotion): Double {
        return when (val promo = item.promotion) {
            is ProductPromotion.Percent -> promo.percent
            else -> 0.0
        }
    }

    private fun effectivePrice(item: ProductWithPromotion): Double {
        return when (val promo = item.promotion) {
            is ProductPromotion.Percent -> promo.discountedPrice
            else -> item.product.price
        }

    }
}