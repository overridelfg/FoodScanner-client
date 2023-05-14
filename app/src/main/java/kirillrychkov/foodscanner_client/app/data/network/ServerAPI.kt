package kirillrychkov.foodscanner_client.app.data.network

import kirillrychkov.foodscanner_client.app.data.network.models.*
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query as QueryRetro


interface ServerAPI {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequestDTO: LoginRequestDTO
    ) : Response<LoginResponseDTO>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequestDTO: RegisterRequestDTO
    ) : Response<LoginResponseDTO>

    @POST("/auth/refresh-token")
    suspend fun refreshAccessToken(
        @Header("x-refresh-token") refreshToken: String
    ): Response<TokensResponseDTO>

    @GET("restrictions/diets")
    suspend fun getDiets(
    ) : List<DietDTO>

    @GET("restrictions/allergens")
    suspend fun getAllergens(
    ) : List<AllergenDTO>

    @GET("user/diets")
    suspend fun getUserDiets(
        @Header("Authorization") token: String
    ) : Response<List<DietDTO>>

    @GET("user/allergens")
    suspend fun getUserAllergens(
        @Header("Authorization") token: String
    ) : Response<List<AllergenDTO>>

    @POST("user/updateRestrictions")
    suspend fun updateRestrictions(
        @Header("Authorization") token: String,
        @Body userRestrictionsDTO: UserRestrictionsDTO
    ) : Response<SuccessResponseDTO>


    @GET("products/details/{barcode}")
    suspend fun getProductDetails(
        @Header("Authorization") token: String,
        @Path("barcode") barcode: Long
    ) : Response<ProductDTO>

    @GET("products/list")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<List<ProductDTO>>

    @GET("products/search")
    suspend fun getProductBySearch(
        @Header("Authorization") token: String,
        @QueryRetro("name") name: String
    ): Response<List<ProductDTO>>

    @GET("products/isProductValid/{product_id}")
    suspend fun getProductRestrictionsDetails(
        @Header("Authorization") token: String,
        @Path("product_id") productId: Long
    ): Response<ProductRestrictionResponseDTO>

    @PUT("products/addToFavorite")
    suspend fun addToFavorite(
        @Header("Authorization") token: String,
        @QueryRetro("productId") productId: Long
    ): Response<SuccessResponseDTO>

    @GET("products/favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): Response<List<ProductDTO>>

    @GET("products/barcodeScanHistory")
    suspend fun getBarcodeScanHistory(
        @Header("Authorization") token: String
    ): Response<List<ProductDTO>>


}