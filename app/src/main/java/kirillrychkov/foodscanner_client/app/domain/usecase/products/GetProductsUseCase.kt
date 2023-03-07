package kirillrychkov.foodscanner_client.app.domain.usecase.products

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    val productsRepository: ProductsRepository
) {
    suspend operator fun invoke() : OperationResult<List<Product>, String?>{
        return productsRepository.getProducts()
    }
}