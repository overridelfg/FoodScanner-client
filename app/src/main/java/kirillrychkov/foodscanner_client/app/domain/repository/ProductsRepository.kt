package kirillrychkov.foodscanner_client.app.domain.repository

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product

interface ProductsRepository {
    suspend fun getProductDetails(barcode: Long): OperationResult<Product, String?>
}