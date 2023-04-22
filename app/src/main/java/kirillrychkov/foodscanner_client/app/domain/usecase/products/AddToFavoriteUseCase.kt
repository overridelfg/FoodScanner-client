package kirillrychkov.foodscanner_client.app.domain.usecase.products

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    suspend operator fun invoke(productId: Long): OperationResult<SuccessResponse, String?> {
        return productsRepository.addToFavorite(productId)
    }
}