package kirillrychkov.foodscanner_client.app.domain.usecase.products

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductRestrictionsDetailsUseCase @Inject constructor(
    val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(product_id: Long) : OperationResult<ProductRestriction, String?> {
        return productsRepository.getProductRestrictionsDetails(product_id)
    }
}