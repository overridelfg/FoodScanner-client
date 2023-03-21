package kirillrychkov.foodscanner_client.app.domain.repository

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product

interface ProductsRepository {
    suspend fun getProductDetails(barcode: Long): OperationResult<Product, String?>

    suspend fun getProducts() : OperationResult<List<Product>, String?>

    suspend fun getProductsBySearch(name: String) : OperationResult<List<Product>, String?>
}