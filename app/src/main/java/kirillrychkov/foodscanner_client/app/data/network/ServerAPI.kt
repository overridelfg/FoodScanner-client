package kirillrychkov.foodscanner_client.app.data.network

import kirillrychkov.foodscanner_client.app.data.network.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


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

    @GET("restrictions/userRestrictions")
    suspend fun getUserRestrictions(
        @Header("Authorization") token: String
    ): UserRestrictionsDTO

//    @POST("restrictions/postUserRestrictions")
    suspend fun postUserRestrictions(
        userRestrictionsDTO: UserRestrictionsDTO
    ): UserRestrictionsDTO
}