package kirillrychkov.foodscanner_client.app.data.network

import kirillrychkov.foodscanner_client.app.data.network.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface ServerAPI {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequestDTO: LoginRequestDTO
    ) : LoginResponseDTO

    @POST("auth/register")
    suspend fun register(
        @Body registerRequestDTO: RegisterRequestDTO
    ) : LoginResponseDTO

    @GET("restrictions/diets")
    suspend fun getDiets(
    ) : List<DietDTO>

    @GET("restrictions/allergens")
    suspend fun getAllergens(
    ) : List<AllergenDTO>

    @GET("products/details/{barcode}")
    suspend fun getProductDetails(
        @Path("barcode") barcode: Long
    ) : ProductDTO

    @GET("products/list")
    suspend fun getProducts(
    ): List<ProductDTO>
}