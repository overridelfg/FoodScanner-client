package kirillrychkov.foodscanner_client.app.domain.usecase.scan

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import javax.inject.Inject

class GetBarcodeScanHistoryUseCase  @Inject constructor(
    val productsRepository: ProductsRepository
) {
    suspend operator fun invoke() : OperationResult<List<Product>, String?> {
        return productsRepository.getBarcodeScanHistory()
    }
}