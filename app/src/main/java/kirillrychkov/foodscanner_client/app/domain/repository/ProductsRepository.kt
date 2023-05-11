package kirillrychkov.foodscanner_client.app.domain.repository

import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse

interface ProductsRepository {
    suspend fun getProductDetails(barcode: Long): OperationResult<Product, String?>

    suspend fun getProducts() : OperationResult<List<Product>, String?>

    suspend fun getProductsBySearch(name: String) : OperationResult<List<Product>, String?>

    suspend fun getProductRestrictionsDetails(product_id: Long) : OperationResult<ProductRestriction, String?>

    suspend fun addToFavorite(productId: Long): OperationResult<SuccessResponse, String?>

    suspend fun getFavorites(): OperationResult<List<Product>, String?>

    suspend fun getBarcodeScanHistory(): OperationResult<List<Product>, String?>
}