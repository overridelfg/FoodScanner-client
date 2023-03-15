package kirillrychkov.foodscanner_client.app.data.repository

import kirillrychkov.foodscanner_client.app.data.network.ServerAPI
import kirillrychkov.foodscanner_client.app.data.network.models.ProductDetailsRequest
import kirillrychkov.foodscanner_client.app.data.network.models.toDiet
import kirillrychkov.foodscanner_client.app.data.network.models.toProduct
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ServerAPI
) : ProductsRepository {
    override suspend fun getProductDetails(barcode: Long): OperationResult<Product, String?> {
        return withContext(Dispatchers.IO){
            try{
                val result = apiService.getProductDetails(barcode).toProduct()
                return@withContext OperationResult.Success(result)
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getProducts(): OperationResult<List<Product>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val result = apiService.getProducts().map{
                    it.toProduct()
                }
                return@withContext OperationResult.Success(result)
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }
}