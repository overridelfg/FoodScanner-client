package kirillrychkov.foodscanner_client.app.data.repository

import android.graphics.Bitmap
import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.ServerAPI
import kirillrychkov.foodscanner_client.app.data.network.models.*
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.FeedbackImages
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction
import kirillrychkov.foodscanner_client.app.domain.entity.SuccessResponse
import kirillrychkov.foodscanner_client.app.domain.repository.ProductsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.net.ConnectException
import javax.inject.Inject


class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ServerAPI,
    val prefsStorage: PrefsStorage
) : ProductsRepository {
    override suspend fun getProductDetails(barcode: Long): OperationResult<Product, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getProductDetails(token, barcode)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.toProduct()
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("message") == "Not Found"){
                        return@withContext OperationResult.Error(errorObj.getString("message"))
                    }
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getProductDetails(barcode)
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause.toString().split(':')[0])
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getProducts(): OperationResult<List<Product>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getProducts(token)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map {
                        it.toProduct()
                    }
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                            refreshToken()
                            getProducts()
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }

                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getProductsBySearch(name: String): OperationResult<List<Product>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getProductBySearch(token, name, 0)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map{
                        it.toProduct()
                    }
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getProductsBySearch(name)
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getProductRestrictionsDetails(product_id: Long): OperationResult<ProductRestriction, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getProductRestrictionsDetails(token, product_id)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.toProductRestriction()
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getProductRestrictionsDetails(product_id)
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun addToFavorite(productId: Long): OperationResult<SuccessResponse, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.addToFavorite(token, productId)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.toSuccessResponse()
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        addToFavorite(productId)
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getFavorites(): OperationResult<List<Product>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getFavorites(token)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map {
                        it.toProduct()
                    }
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getFavorites()
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }

                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }

            }
            catch (e: HttpException){
                return@withContext OperationResult.Error(e.cause.toString().split(':')[0])
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause.toString().split(':')[0])
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun getBarcodeScanHistory(): OperationResult<List<Product>, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                val response = apiService.getBarcodeScanHistory(token)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map {
                        it.toProduct()
                    }
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        getBarcodeScanHistory()
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }

                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }
            catch (e: IOException){
                return@withContext OperationResult.Error(e.cause.toString().split(':')[0])
            }
            catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    override suspend fun feedbackNonexistentProducts(feedBack: FeedbackImages): OperationResult<SuccessResponse, String?> {
        return withContext(Dispatchers.IO){
            try{
                val token = "Bearer " + prefsStorage.getUser()!!.accessToken

                val response = apiService.postNonexistentProductFeedback(token)
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.toSuccessResponse()
                    return@withContext OperationResult.Success(result)
                }else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if(errorObj.getString("error") == "Token expired!"){
                        refreshToken()
                        feedbackNonexistentProducts(feedBack)
                    }else{
                        return@withContext OperationResult.Error(errorObj.getString("error"))
                    }
                } else {
                    return@withContext OperationResult.Error("Что-то пошло не так!")
                }
            }catch (e: Exception){
                return@withContext OperationResult.Error(e.message)
            }
        }
    }

    private suspend fun refreshToken(){
        withContext(Dispatchers.IO) {
            var refreshToken = ""
            synchronized(this){
                refreshToken = prefsStorage.getUser()!!.refreshToken
            }
            val result = apiService.refreshAccessToken(refreshToken).body()!!
            prefsStorage.refreshTokens(result)
        }
    }
}