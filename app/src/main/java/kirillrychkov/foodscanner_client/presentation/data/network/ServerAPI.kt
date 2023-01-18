package kirillrychkov.foodscanner_client.presentation.data.network

import kirillrychkov.foodscanner_client.presentation.data.network.models.*
import retrofit2.http.GET
import retrofit2.http.POST


interface ServerAPI {
    @POST("login")
    suspend fun login(
        loginRequestDTO: LoginRequestDTO
    ) : LoginResponseDTO

    @POST("register")
    suspend fun register(
        registerRequestDTO: RegisterRequestDTO
    ) : LoginResponseDTO

    @GET("restrictions/diets")
    suspend fun getDiets(

    ) : List<DietDTO>

    @GET("restrictions/allergens")
    suspend fun getAllergens(

    ) : List<AllergenDTO>
}