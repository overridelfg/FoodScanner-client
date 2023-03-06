package kirillrychkov.foodscanner_client.app.domain.usecase.products

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(barcode: Long): OperationResult<Product, String?>{
        return productsRepository.getProductDetails(barcode)
    }
}