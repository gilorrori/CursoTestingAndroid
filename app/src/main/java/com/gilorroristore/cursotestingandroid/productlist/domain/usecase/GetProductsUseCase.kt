package com.gilorroristore.cursotestingandroid.productlist.domain.usecase

import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke() : Flow<List<Product>>{
        return productRepository.getProducts()
    }
}